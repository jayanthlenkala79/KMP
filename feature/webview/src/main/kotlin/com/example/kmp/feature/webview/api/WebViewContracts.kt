package com.example.kmp.feature.webview.api

/**
 * WebView events that can be emitted during usage
 */
sealed interface WebEvent {
    data class PageReady(val url: String) : WebEvent
    data class Navigation(val url: String) : WebEvent
    data class JsMessage(val name: String, val payload: String) : WebEvent
    data class DownloadRequested(val url: String, val mime: String?, val filename: String?) : WebEvent
    data class UploadRequested(val acceptTypes: List<String>, val multiple: Boolean) : WebEvent
    data class Error(val code: Int?, val description: String) : WebEvent
    data class Interruption(val kind: Kind, val detail: String? = null) : WebEvent {
        enum class Kind { 
            SslError, 
            NetworkLost, 
            BackPressed, 
            NavigationBlocked, 
            UploadCanceled, 
            DownloadFailed 
        }
    }
}

/**
 * Commands that can be sent to the WebView
 */
sealed interface WebCommand {
    data class LoadUrl(val url: String) : WebCommand
    data class PostMessage(val name: String, val json: String) : WebCommand
    data object GoBack : WebCommand
    data object Reload : WebCommand
}

/**
 * Main interface for managing WebView instances
 */
interface WebViewManager {
    /**
     * Send a command to the current WebView instance (if attached)
     */
    fun send(cmd: WebCommand)
    
    /**
     * Open a full-screen WebView with the specified configuration
     */
    fun open(
        startUrl: String,
        title: String = "Web",
        allowHosts: Set<String> = emptySet(),
        onEvent: (WebEvent) -> Unit = {}
    )
}
