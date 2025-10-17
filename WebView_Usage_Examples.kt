/**
 * WebView Usage Examples
 * 
 * This file demonstrates various ways to use the WebView feature in your Android app.
 * The WebView module provides a clean, reusable interface for displaying web content
 * with advanced features like file upload/download, security policies, and event handling.
 */

package com.example.kmp.examples

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import com.example.kmp.feature.webview.api.WebViewManager
import com.example.kmp.feature.webview.api.WebEvent
import com.example.kmp.feature.webview.api.WebCommand

// ============================================================================
// EXAMPLE 1: Basic WebView Usage
// ============================================================================

@Composable
fun BasicWebViewExample() {
    val webViewManager: WebViewManager = koinInject()
    
    Button(
        onClick = {
            webViewManager.open(
                startUrl = "https://www.github.com",
                title = "GitHub"
            )
        }
    ) {
        Text("Open GitHub")
    }
}

// ============================================================================
// EXAMPLE 2: WebView with Event Handling
// ============================================================================

@Composable
fun WebViewWithEventsExample() {
    val webViewManager: WebViewManager = koinInject()
    var lastEvent by remember { mutableStateOf<String?>(null) }
    
    Column {
        Button(
            onClick = {
                webViewManager.open(
                    startUrl = "https://example.com",
                    title = "Example Site",
                    allowHosts = setOf("example.com", "www.example.com"),
                    onEvent = { event ->
                        lastEvent = when (event) {
                            is WebEvent.PageReady -> "Page loaded: ${event.url}"
                            is WebEvent.Navigation -> "Navigated to: ${event.url}"
                            is WebEvent.DownloadRequested -> "Download: ${event.filename}"
                            is WebEvent.UploadRequested -> "Upload requested"
                            is WebEvent.Error -> "Error: ${event.description}"
                            is WebEvent.Interruption -> "Interruption: ${event.kind}"
                            is WebEvent.JsMessage -> "JS Message: ${event.name}"
                        }
                    }
                )
            }
        ) {
            Text("Open with Events")
        }
        
        if (lastEvent != null) {
            Text(
                text = "Last Event: $lastEvent",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

// ============================================================================
// EXAMPLE 3: WebView with Custom Host Allowlist
// ============================================================================

@Composable
fun WebViewWithSecurityExample() {
    val webViewManager: WebViewManager = koinInject()
    
    Button(
        onClick = {
            webViewManager.open(
                startUrl = "https://developer.android.com",
                title = "Android Developers",
                allowHosts = setOf(
                    "developer.android.com",
                    "www.developer.android.com",
                    "source.android.com"
                ),
                onEvent = { event ->
                    if (event is WebEvent.Interruption && 
                        event.kind == WebEvent.Interruption.Kind.NavigationBlocked) {
                        // Handle blocked navigation
                        println("Navigation blocked to: ${event.detail}")
                    }
                }
            )
        }
    ) {
        Text("Open with Security Policy")
    }
}

// ============================================================================
// EXAMPLE 4: Sending Commands to WebView
// ============================================================================

@Composable
fun WebViewCommandsExample() {
    val webViewManager: WebViewManager = koinInject()
    
    Column(spacing = 8.dp) {
        Button(
            onClick = {
                webViewManager.open(
                    startUrl = "https://www.google.com",
                    title = "Google"
                )
            }
        ) {
            Text("Open Google")
        }
        
        Button(
            onClick = {
                // Send a JavaScript message to the WebView
                webViewManager.send(
                    WebCommand.PostMessage(
                        name = "hello",
                        json = """{"message": "Hello from native!", "timestamp": "${System.currentTimeMillis()}"}"""
                    )
                )
            }
        ) {
            Text("Send JS Message")
        }
        
        Button(
            onClick = {
                // Reload the current WebView
                webViewManager.send(WebCommand.Reload)
            }
        ) {
            Text("Reload Page")
        }
        
        Button(
            onClick = {
                // Go back in WebView history
                webViewManager.send(WebCommand.GoBack)
            }
        ) {
            Text("Go Back")
        }
    }
}

// ============================================================================
// EXAMPLE 5: File Upload/Download Handling
// ============================================================================

@Composable
fun WebViewFileHandlingExample() {
    val webViewManager: WebViewManager = koinInject()
    var uploadStatus by remember { mutableStateOf<String?>(null) }
    var downloadStatus by remember { mutableStateOf<String?>(null) }
    
    Column {
        Button(
            onClick = {
                webViewManager.open(
                    startUrl = "https://www.w3schools.com/html/html_file_upload.asp",
                    title = "File Upload Test",
                    onEvent = { event ->
                        when (event) {
                            is WebEvent.UploadRequested -> {
                                uploadStatus = "Upload requested: ${event.acceptTypes.joinToString()}"
                                // The native file picker will automatically open
                            }
                            is WebEvent.DownloadRequested -> {
                                downloadStatus = "Download started: ${event.filename}"
                                // The download will be automatically handled by DownloadManager
                            }
                            else -> {}
                        }
                    }
                )
            }
        ) {
            Text("Test File Upload/Download")
        }
        
        if (uploadStatus != null) {
            Text(
                text = uploadStatus!!,
                modifier = Modifier.padding(8.dp)
            )
        }
        
        if (downloadStatus != null) {
            Text(
                text = downloadStatus!!,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

// ============================================================================
// EXAMPLE 6: Error Handling and User Interruptions
// ============================================================================

@Composable
fun WebViewErrorHandlingExample() {
    val webViewManager: WebViewManager = koinInject()
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    Column {
        Button(
            onClick = {
                webViewManager.open(
                    startUrl = "https://invalid-url-that-does-not-exist.com",
                    title = "Error Test",
                    onEvent = { event ->
                        when (event) {
                            is WebEvent.Error -> {
                                errorMessage = "Error ${event.code}: ${event.description}"
                            }
                            is WebEvent.Interruption -> {
                                errorMessage = when (event.kind) {
                                    WebEvent.Interruption.Kind.SslError -> "SSL Error: ${event.detail}"
                                    WebEvent.Interruption.Kind.NetworkLost -> "Network connection lost"
                                    WebEvent.Interruption.Kind.BackPressed -> "User pressed back"
                                    WebEvent.Interruption.Kind.NavigationBlocked -> "Navigation blocked: ${event.detail}"
                                    WebEvent.Interruption.Kind.UploadCanceled -> "File upload canceled"
                                    WebEvent.Interruption.Kind.DownloadFailed -> "Download failed"
                                }
                            }
                            else -> {}
                        }
                    }
                )
            }
        ) {
            Text("Test Error Handling")
        }
        
        if (errorMessage != null) {
            Card(
                modifier = Modifier.padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = errorMessage!!,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

// ============================================================================
// EXAMPLE 7: Complete WebView Integration in a Screen
// ============================================================================

@Composable
fun CompleteWebViewIntegrationExample() {
    val webViewManager: WebViewManager = koinInject()
    var isWebViewOpen by remember { mutableStateOf(false) }
    var currentUrl by remember { mutableStateOf("") }
    var events by remember { mutableStateOf<List<String>>(emptyList()) }
    
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Complete WebView Integration",
            style = MaterialTheme.typography.headlineSmall
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    isWebViewOpen = true
                    currentUrl = "https://www.github.com"
                    webViewManager.open(
                        startUrl = currentUrl,
                        title = "GitHub",
                        allowHosts = setOf("github.com", "www.github.com"),
                        onEvent = { event ->
                            events = events + when (event) {
                                is WebEvent.PageReady -> "✓ Page loaded: ${event.url}"
                                is WebEvent.Navigation -> "→ Navigated: ${event.url}"
                                is WebEvent.DownloadRequested -> "⬇ Download: ${event.filename}"
                                is WebEvent.UploadRequested -> "⬆ Upload requested"
                                is WebEvent.Error -> "❌ Error: ${event.description}"
                                is WebEvent.Interruption -> "⚠ ${event.kind}: ${event.detail}"
                                is WebEvent.JsMessage -> "💬 JS: ${event.name}"
                            }
                        }
                    )
                }
            ) {
                Text("Open GitHub")
            }
            
            Button(
                onClick = {
                    webViewManager.send(WebCommand.PostMessage(
                        name = "ping",
                        json = """{"message": "Hello from Android!"}"""
                    ))
                },
                enabled = isWebViewOpen
            ) {
                Text("Send Message")
            }
            
            Button(
                onClick = {
                    webViewManager.send(WebCommand.Reload)
                },
                enabled = isWebViewOpen
            ) {
                Text("Reload")
            }
        }
        
        if (events.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Events:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    events.takeLast(5).forEach { event ->
                        Text(
                            text = event,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

// ============================================================================
// NAVIGATION INTEGRATION EXAMPLES
// ============================================================================

/**
 * Example of how to integrate WebView with Compose Navigation
 */

// In your NavHost:
/*
NavHost(navController, startDestination = "home") {
    composable("home") { HomeScreen() }
    
    composable("webview?url={url}&title={title}") { backStackEntry ->
        val url = backStackEntry.arguments?.getString("url")!!
        val title = backStackEntry.arguments?.getString("title")!!
        
        WebViewScreen(
            title = title,
            startUrl = url,
            onClose = { navController.popBackStack() }
        )
    }
}
*/

// In your screen:
/*
@Composable
fun HomeScreen(navController: NavController) {
    Button(
        onClick = {
            val encodedUrl = URLEncoder.encode("https://example.com", "UTF-8")
            val encodedTitle = URLEncoder.encode("Example", "UTF-8")
            navController.navigate("webview?url=$encodedUrl&title=$encodedTitle")
        }
    ) {
        Text("Open WebView")
    }
}
*/