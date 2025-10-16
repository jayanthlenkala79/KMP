package com.example.kmp.domain

/**
 * Business rules and policies for WebView behavior
 */
object WebViewPolicy {
    
    /**
     * Default allowed hosts for secure navigation
     */
    val DEFAULT_ALLOWED_HOSTS = setOf(
        "app.example.com",
        "static.example.com",
        "cdn.example.com"
    )
    
    /**
     * Security policy for WebView configuration
     */
    object Security {
        const val REQUIRE_HTTPS = true
        const val BLOCK_MIXED_CONTENT = true
        const val DISABLE_FILE_ACCESS = true
        const val DISABLE_CONTENT_ACCESS = true
        const val ENABLE_JAVASCRIPT = true
        const val ENABLE_DOM_STORAGE = true
    }
    
    /**
     * Navigation policy
     */
    object Navigation {
        const val BLOCK_EXTERNAL_NAVIGATION = true
        const val ALLOW_BACK_NAVIGATION = true
    }
    
    /**
     * File handling policy
     */
    object FileHandling {
        const val ENABLE_UPLOAD = true
        const val ENABLE_DOWNLOAD = true
        const val MAX_UPLOAD_SIZE_MB = 50L
        const val ALLOWED_UPLOAD_TYPES = setOf(
            "image/*",
            "application/pdf",
            "text/plain",
            "application/json"
        )
    }
    
    /**
     * Monitoring policy
     */
    object Monitoring {
        const val TRACE_PAGE_LOAD = true
        const val TRACE_NAVIGATION = true
        const val TRACE_ERRORS = true
        const val TRACE_INTERRUPTIONS = true
    }
}
