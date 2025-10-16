package com.example.kmp.data

import android.content.Context
import android.content.SharedPreferences
import com.example.kmp.domain.WebViewPolicy

/**
 * Data layer for WebView preferences and settings
 */
class WebViewPreferences(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "webview_prefs", 
        Context.MODE_PRIVATE
    )
    
    /**
     * Get allowed hosts for navigation
     */
    fun getAllowedHosts(): Set<String> {
        val hosts = prefs.getStringSet(KEY_ALLOWED_HOSTS, WebViewPolicy.DEFAULT_ALLOWED_HOSTS)
        return hosts ?: WebViewPolicy.DEFAULT_ALLOWED_HOSTS
    }
    
    /**
     * Set allowed hosts for navigation
     */
    fun setAllowedHosts(hosts: Set<String>) {
        prefs.edit()
            .putStringSet(KEY_ALLOWED_HOSTS, hosts)
            .apply()
    }
    
    /**
     * Get last visited URL for restoration
     */
    fun getLastUrl(): String? {
        return prefs.getString(KEY_LAST_URL, null)
    }
    
    /**
     * Set last visited URL
     */
    fun setLastUrl(url: String) {
        prefs.edit()
            .putString(KEY_LAST_URL, url)
            .apply()
    }
    
    /**
     * Check if JavaScript is enabled
     */
    fun isJavaScriptEnabled(): Boolean {
        return prefs.getBoolean(KEY_JAVASCRIPT_ENABLED, WebViewPolicy.Security.ENABLE_JAVASCRIPT)
    }
    
    /**
     * Set JavaScript enabled state
     */
    fun setJavaScriptEnabled(enabled: Boolean) {
        prefs.edit()
            .putBoolean(KEY_JAVASCRIPT_ENABLED, enabled)
            .apply()
    }
    
    companion object {
        private const val KEY_ALLOWED_HOSTS = "allowed_hosts"
        private const val KEY_LAST_URL = "last_url"
        private const val KEY_JAVASCRIPT_ENABLED = "javascript_enabled"
    }
}
