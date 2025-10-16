package com.example.kmp.feature.webview.internal

import com.example.kmp.feature.webview.api.WebCommand
import com.example.kmp.feature.webview.api.WebEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Controller for managing WebView events and commands
 */
class WebController {
    private val _events = MutableSharedFlow<WebEvent>(extraBufferCapacity = 64)
    val events = _events.asSharedFlow()
    
    private val _commands = MutableSharedFlow<WebCommand>(extraBufferCapacity = 64)
    val commands = _commands.asSharedFlow()
    
    /**
     * Emit an event to observers
     */
    fun emit(event: WebEvent) {
        _events.tryEmit(event)
    }
    
    /**
     * Send a command to the WebView
     */
    fun send(command: WebCommand) {
        _commands.tryEmit(command)
    }
}
