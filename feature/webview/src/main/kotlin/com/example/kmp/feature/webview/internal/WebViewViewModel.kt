package com.example.kmp.feature.webview.internal

import androidx.lifecycle.ViewModel
import com.example.kmp.feature.webview.api.WebCommand

/**
 * ViewModel for WebView screen following MVVM pattern
 */
class WebViewViewModel(
    private val controller: WebController
) : ViewModel() {
    
    /**
     * Handle commands from the UI
     */
    fun onCommand(cmd: WebCommand) {
        controller.send(cmd)
    }
    
    /**
     * Get events from the controller
     */
    fun getEvents() = controller.events
}
