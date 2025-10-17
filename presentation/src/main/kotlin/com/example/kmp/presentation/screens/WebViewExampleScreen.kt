package com.example.kmp.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.kmp.feature.webview.api.WebEvent
import com.example.kmp.feature.webview.api.WebViewManager
import org.koin.compose.koinInject
import java.net.URLEncoder

/**
 * Example screen showing different ways to open WebView from Jetpack Compose
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewExampleScreen(navController: NavHostController) {
    val webViewManager: WebViewManager = koinInject()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("WebView Examples") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("←")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            
            Text(
                text = "WebView Navigation Examples",
                style = MaterialTheme.typography.headlineMedium
            )
            
            // Method 1: Navigation-based WebView (Recommended)
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val url = "https://www.github.com"
                    val title = "GitHub"
                    val encodedUrl = URLEncoder.encode(url, "UTF-8")
                    val encodedTitle = URLEncoder.encode(title, "UTF-8")
                    navController.navigate("webview?url=$encodedUrl&title=$encodedTitle")
                }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. Navigation-based WebView",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Opens GitHub using navigation route",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Method 2: Manager-based WebView with Event Handling
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    webViewManager.open(
                        startUrl = "https://developer.android.com",
                        title = "Android Developers",
                        allowHosts = setOf("developer.android.com", "www.developer.android.com"),
                        onEvent = { event ->
                            when (event) {
                                is WebEvent.PageReady -> {
                                    println("✅ Page loaded: ${event.url}")
                                }
                                is WebEvent.DownloadRequested -> {
                                    println("📥 Download: ${event.filename}")
                                }
                                is WebEvent.UploadRequested -> {
                                    println("📤 Upload: ${event.acceptTypes}")
                                }
                                is WebEvent.Error -> {
                                    println("❌ Error: ${event.description}")
                                }
                                is WebEvent.Interruption -> {
                                    println("⚠️ Interruption: ${event.kind}")
                                }
                                is WebEvent.Navigation -> {
                                    println("🧭 Navigation: ${event.url}")
                                }
                                is WebEvent.JsMessage -> {
                                    println("📨 JS Message: ${event.name}")
                                }
                            }
                        }
                    )
                }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "2. Manager-based WebView",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Opens Android Developers with event handling",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Method 3: Custom URL input
            var customUrl by remember { mutableStateOf("https://www.google.com") }
            var customTitle by remember { mutableStateOf("Google") }
            
            Text(
                text = "3. Custom WebView",
                style = MaterialTheme.typography.titleMedium
            )
            
            OutlinedTextField(
                value = customUrl,
                onValueChange = { customUrl = it },
                label = { Text("URL") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = customTitle,
                onValueChange = { customTitle = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Button(
                onClick = {
                    val encodedUrl = URLEncoder.encode(customUrl, "UTF-8")
                    val encodedTitle = URLEncoder.encode(customTitle, "UTF-8")
                    navController.navigate("webview?url=$encodedUrl&title=$encodedTitle")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Open Custom WebView")
            }
            
            // Method 4: WebView with Security Restrictions
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    webViewManager.open(
                        startUrl = "https://www.example.com",
                        title = "Example (Secure)",
                        allowHosts = setOf("example.com"), // Only allow this host
                        onEvent = { event ->
                            when (event) {
                                is WebEvent.Interruption -> {
                                    if (event.kind == WebEvent.Interruption.Kind.NavigationBlocked) {
                                        println("🚫 Navigation blocked to: ${event.detail}")
                                    }
                                }
                                else -> {
                                    // Handle other events
                                }
                            }
                        }
                    )
                }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "4. Secure WebView",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Opens with host restrictions",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Text(
                text = "💡 Tips:\n• Use navigation for simple WebView opening\n• Use manager for advanced event handling\n• Always encode URLs when using navigation\n• Set allowHosts for security",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
