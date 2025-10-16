package com.example.kmp.feature.webview.internal.ui

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.webkit.*
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.kmp.domain.WebViewPolicy
import com.example.kmp.feature.webview.api.WebCommand
import com.example.kmp.feature.webview.api.WebEvent
import com.example.kmp.feature.webview.internal.WebController
import com.example.kmp.feature.webview.internal.WebViewViewModel
import com.example.kmp.monitoring.Monitoring
import org.koin.compose.koinInject

/**
 * Main WebView screen with full functionality
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    title: String,
    startUrl: String,
    allowHosts: Set<String>,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val monitoring: Monitoring = koinInject()
    
    // Create controller and ViewModel
    val controller = remember { WebController() }
    val viewModel: WebViewViewModel = viewModel { WebViewViewModel(controller) }
    
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var chooserCallback by remember { mutableStateOf<ValueCallback<Array<Uri>>?>(null) }
    
    // File chooser launcher
    val fileChooserLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        chooserCallback?.onReceiveValue(uri?.let { arrayOf(it) })
        chooserCallback = null
    }
    
    val multipleFileChooserLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        chooserCallback?.onReceiveValue(uris.toTypedArray())
        chooserCallback = null
    }
    
    // Handle commands from manager
    LaunchedEffect(Unit) {
        controller.commands.collect { cmd ->
            val webView = webViewRef ?: return@collect
            when (cmd) {
                is WebCommand.LoadUrl -> webView.loadUrl(cmd.url)
                is WebCommand.PostMessage -> {
                    webView.post {
                        webView.evaluateJavascript(
                            "window.app?.onNativeMessage('${cmd.name}', '${cmd.json}');",
                            null
                        )
                    }
                }
                WebCommand.GoBack -> if (webView.canGoBack()) webView.goBack()
                WebCommand.Reload -> webView.reload()
            }
        }
    }
    
    // Handle events
    LaunchedEffect(Unit) {
        controller.events.collect { event ->
            when (event) {
                is WebEvent.PageReady -> {
                    monitoring.trace("web_page_ready", mapOf("url" to event.url))
                }
                is WebEvent.Navigation -> {
                    monitoring.trace("web_navigation", mapOf("url" to event.url))
                }
                is WebEvent.DownloadRequested -> {
                    enqueueDownload(context, event.url, event.mime, event.filename)
                    monitoring.trace("web_download_requested", mapOf(
                        "url" to event.url,
                        "mime" to (event.mime ?: ""),
                        "filename" to (event.filename ?: "")
                    ))
                }
                is WebEvent.UploadRequested -> {
                    monitoring.trace("web_upload_requested", mapOf(
                        "types" to event.acceptTypes.joinToString(","),
                        "multiple" to event.multiple.toString()
                    ))
                }
                is WebEvent.Error -> {
                    monitoring.error("web_error", mapOf(
                        "code" to (event.code?.toString() ?: ""),
                        "description" to event.description
                    ))
                }
                is WebEvent.Interruption -> {
                    monitoring.warning("web_interruption", mapOf(
                        "kind" to event.kind.name,
                        "detail" to (event.detail ?: "")
                    ))
                }
                is WebEvent.JsMessage -> {
                    monitoring.info("web_js_message", mapOf(
                        "name" to event.name,
                        "payload" to event.payload
                    ))
                }
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                actions = {
                    IconButton(onClick = { controller.send(WebCommand.Reload) }) {
                        Text("↻")
                    }
                }
            )
        }
    ) { paddingValues ->
        AndroidView(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            factory = { ctx ->
                WebView(ctx).apply {
                    settings.apply {
                        javaScriptEnabled = WebViewPolicy.Security.ENABLE_JAVASCRIPT
                        domStorageEnabled = WebViewPolicy.Security.ENABLE_DOM_STORAGE
                        allowFileAccess = !WebViewPolicy.Security.DISABLE_FILE_ACCESS
                        allowContentAccess = !WebViewPolicy.Security.DISABLE_CONTENT_ACCESS
                        mixedContentMode = if (WebViewPolicy.Security.BLOCK_MIXED_CONTENT) {
                            WebSettings.MIXED_CONTENT_NEVER_ALLOW
                        } else {
                            WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        }
                        setNetworkAvailable(true)
                        
                        // Security settings
                        setGeolocationEnabled(false)
                        setAppCacheEnabled(false)
                        setDatabaseEnabled(false)
                        setSaveFormData(false)
                        setSavePassword(false)
                    }
                    
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView, url: String) {
                            controller.emit(WebEvent.PageReady(url))
                        }
                        
                        override fun shouldOverrideUrlLoading(
                            view: WebView,
                            request: WebResourceRequest
                        ): Boolean {
                            val host = request.url.host ?: return true
                            val allowedHosts = if (allowHosts.isEmpty()) WebViewPolicy.DEFAULT_ALLOWED_HOSTS else allowHosts
                            val isAllowed = host in allowedHosts
                            
                            if (!isAllowed) {
                                controller.emit(
                                    WebEvent.Interruption(
                                        WebEvent.Interruption.Kind.NavigationBlocked,
                                        request.url.toString()
                                    )
                                )
                            }
                            
                            return !isAllowed
                        }
                        
                        override fun onReceivedError(
                            view: WebView,
                            request: WebResourceRequest,
                            error: WebResourceError
                        ) {
                            controller.emit(WebEvent.Error(error.errorCode, error.description.toString()))
                        }
                        
                        override fun onReceivedSslError(
                            view: WebView,
                            handler: SslErrorHandler,
                            error: SslError
                        ) {
                            handler.cancel()
                            controller.emit(
                                WebEvent.Interruption(
                                    WebEvent.Interruption.Kind.SslError,
                                    error.primaryError.toString()
                                )
                            )
                        }
                        
                        override fun onReceivedHttpError(
                            view: WebView,
                            request: WebResourceRequest,
                            errorResponse: WebResourceResponse
                        ) {
                            controller.emit(WebEvent.Error(errorResponse.statusCode, "HTTP Error"))
                        }
                    }
                    
                    webChromeClient = object : WebChromeClient() {
                        override fun onShowFileChooser(
                            webView: WebView?,
                            filePathCallback: ValueCallback<Array<Uri>>?,
                            fileChooserParams: FileChooserParams?
                        ): Boolean {
                            chooserCallback = filePathCallback
                            val types = fileChooserParams?.acceptTypes?.filter { it.isNotBlank() } ?: emptyList()
                            val isMultiple = fileChooserParams?.mode == FileChooserParams.MODE_OPEN_MULTIPLE
                            
                            // Launch appropriate file chooser
                            if (isMultiple) {
                                multipleFileChooserLauncher.launch(types.toTypedArray())
                            } else {
                                fileChooserLauncher.launch(types.toTypedArray())
                            }
                            
                            controller.emit(WebEvent.UploadRequested(types, isMultiple))
                            return true
                        }
                        
                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                            // Could emit progress events here if needed
                        }
                    }
                    
                    setDownloadListener { url, userAgent, contentDisposition, mimeType, contentLength ->
                        val filename = URLUtil.guessFileName(url, contentDisposition, mimeType)
                        controller.emit(WebEvent.DownloadRequested(url, mimeType, filename))
                    }
                    
                    // Add JavaScript interface for native communication
                    addJavascriptInterface(object {
                        @JavascriptInterface
                        fun onJsMessage(name: String, payload: String) {
                            controller.emit(WebEvent.JsMessage(name, payload))
                        }
                    }, "Android")
                    
                    loadUrl(startUrl)
                    webViewRef = this
                }
            },
            update = { /* No-op */ }
        )
    }
    
    // Handle back press
    BackHandler {
        val webView = webViewRef
        if (webView?.canGoBack() == true) {
            webView.goBack()
            controller.emit(WebEvent.Interruption(WebEvent.Interruption.Kind.BackPressed))
        } else {
            onClose()
        }
    }
    
    // Cleanup file chooser callback on dispose
    DisposableEffect(Unit) {
        onDispose {
            chooserCallback?.onReceiveValue(null)
            chooserCallback = null
        }
    }
}

/**
 * Helper function to enqueue downloads using DownloadManager
 */
private fun enqueueDownload(
    context: Context,
    url: String,
    mime: String?,
    filename: String?
) {
    try {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(url)).apply {
            mime?.let { setMimeType(it) }
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                filename ?: URLUtil.guessFileName(url, null, mime)
            )
            setTitle(filename ?: "Download")
            setDescription("Downloading file...")
            
            // Add cookies and user agent
            CookieManager.getInstance().getCookie(url)?.let { cookie ->
                addRequestHeader("cookie", cookie)
            }
            addRequestHeader("User-Agent", WebSettings.getDefaultUserAgent(context))
        }
        
        downloadManager.enqueue(request)
    } catch (e: Exception) {
        // Handle download error
    }
}
