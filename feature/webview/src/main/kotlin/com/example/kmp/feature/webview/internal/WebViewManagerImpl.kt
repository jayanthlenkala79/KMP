package com.example.kmp.feature.webview.internal

import androidx.navigation.NavController
import com.example.kmp.feature.webview.api.WebCommand
import com.example.kmp.feature.webview.api.WebEvent
import com.example.kmp.feature.webview.api.WebViewManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.net.URLEncoder

/**
 * Implementation of WebViewManager that handles navigation and command routing
 */
class WebViewManagerImpl(
    private val navController: NavController
) : WebViewManager {
    
    private val scope = MainScope()
    private var controller: WebController? = null
    
    override fun send(cmd: WebCommand) {
        controller?.send(cmd)
    }
    
    override fun open(
        startUrl: String,
        title: String,
        allowHosts: Set<String>,
        onEvent: (WebEvent) -> Unit
    ) {
        scope.launch {
            val encodedUrl = URLEncoder.encode(startUrl, "UTF-8")
            val encodedTitle = URLEncoder.encode(title, "UTF-8")
            
            // Store the event handler for this WebView session
            WebViewSessionManager.setEventHandler(onEvent)
            
            navController.navigate("webview?url=$encodedUrl&title=$encodedTitle")
        }
    }
    
    /**
     * Attach a controller instance (called by the WebView screen)
     */
    fun attachController(controller: WebController) {
        this.controller = controller
        
        // Set up event forwarding
        scope.launch {
            controller.events.collect { event ->
                WebViewSessionManager.getEventHandler()?.invoke(event)
            }
        }
    }
    
    /**
     * Detach controller when screen is destroyed
     */
    fun detachController() {
        this.controller = null
        WebViewSessionManager.clearEventHandler()
    }
}

/**
 * Singleton to manage WebView session state
 */
object WebViewSessionManager {
    private var eventHandler: ((WebEvent) -> Unit)? = null
    
    fun setEventHandler(handler: (WebEvent) -> Unit) {
        eventHandler = handler
    }
    
    fun getEventHandler(): ((WebEvent) -> Unit)? = eventHandler
    
    fun clearEventHandler() {
        eventHandler = null
    }
}
