package com.example.kmp.presentation.screens

import androidx.compose.runtime.Composable
import com.example.kmp.feature.webview.api.WebViewScreenPublic

/**
 * WebView screen wrapper for presentation layer
 */
@Composable
fun WebViewScreen(
    title: String,
    startUrl: String,
    onClose: () -> Unit
) {
    WebViewScreenPublic(
        title = title,
        startUrl = startUrl,
        allowHosts = emptySet(), // Use default policy
        onClose = onClose
    )
}
