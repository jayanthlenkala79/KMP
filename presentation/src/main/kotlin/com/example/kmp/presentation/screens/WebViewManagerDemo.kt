package com.example.kmp.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kmp.feature.webview.api.WebCommand
import com.example.kmp.feature.webview.api.WebEvent
import com.example.kmp.feature.webview.api.WebViewManager
import org.koin.compose.koinInject

/**
 * Demo screen showing direct WebViewManager usage
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewManagerDemo() {
    val webViewManager: WebViewManager = koinInject()
    var lastEvent by remember { mutableStateOf<String?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("WebView Manager Demo") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Direct WebViewManager Usage",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Button(
                onClick = {
                    webViewManager.open(
                        startUrl = "https://www.github.com",
                        title = "GitHub",
                        allowHosts = setOf("github.com", "www.github.com"),
                        onEvent = { event ->
                            lastEvent = when (event) {
                                is WebEvent.PageReady -> "Page Ready: ${event.url}"
                                is WebEvent.Navigation -> "Navigation: ${event.url}"
                                is WebEvent.DownloadRequested -> "Download: ${event.filename}"
                                is WebEvent.UploadRequested -> "Upload Requested"
                                is WebEvent.Error -> "Error: ${event.description}"
                                is WebEvent.Interruption -> "Interruption: ${event.kind}"
                                is WebEvent.JsMessage -> "JS Message: ${event.name}"
                            }
                        }
                    )
                }
            ) {
                Text("Open GitHub with Events")
            }
            
            Button(
                onClick = {
                    webViewManager.send(WebCommand.PostMessage("test", """{"message": "Hello from native!"}"""))
                }
            ) {
                Text("Send JS Message")
            }
            
            Button(
                onClick = {
                    webViewManager.send(WebCommand.Reload)
                }
            ) {
                Text("Reload Current Page")
            }
            
            if (lastEvent != null) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Last Event: $lastEvent",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Text(
                text = "This demonstrates direct WebViewManager usage with event handling and command sending.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
