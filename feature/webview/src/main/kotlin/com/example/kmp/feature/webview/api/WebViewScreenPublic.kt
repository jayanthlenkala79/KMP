package com.example.kmp.feature.webview.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.example.kmp.feature.webview.internal.ui.WebViewScreen
import com.example.kmp.feature.webview.internal.WebController
import com.example.kmp.feature.webview.internal.WebViewManagerImpl
import org.koin.compose.koinInject

/**
 * Public WebView screen that can be used by other modules
 */
@Composable
fun WebViewScreenPublic(
    title: String,
    startUrl: String,
    allowHosts: Set<String> = emptySet(),
    onClose: () -> Unit
) {
    val webViewManager: WebViewManager = koinInject()
    
    // Create controller and attach to manager
    val controller = remember { WebController() }
    
    LaunchedEffect(controller) {
        (webViewManager as? WebViewManagerImpl)?.attachController(controller)
    }
    
    DisposableEffect(Unit) {
        onDispose {
            (webViewManager as? WebViewManagerImpl)?.detachController()
        }
    }
    
    WebViewScreen(
        title = title,
        startUrl = startUrl,
        allowHosts = allowHosts,
        onClose = onClose
    )
}
