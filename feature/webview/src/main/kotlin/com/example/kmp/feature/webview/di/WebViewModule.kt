package com.example.kmp.feature.webview.di

import androidx.navigation.NavController
import com.example.kmp.feature.webview.api.WebViewManager
import com.example.kmp.feature.webview.internal.WebViewManagerImpl
import org.koin.dsl.module

/**
 * Koin module for WebView feature
 */
val webViewModule = module {
    single<WebViewManager> { 
        WebViewManagerImpl(get<NavController>()) 
    }
}
