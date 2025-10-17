package com.example.kmp.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Home screen that demonstrates WebView usage
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenWebView: (url: String, title: String) -> Unit,
    onOpenManagerDemo: () -> Unit,
    onOpenExamples: () -> Unit
) {
    val sampleUrls = listOf(
        "https://www.google.com" to "Google",
        "https://www.github.com" to "GitHub",
        "https://developer.android.com" to "Android Developers",
        "https://www.example.com" to "Example Site"
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("WebView Demo") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "WebView Feature Demo",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            Text(
                text = "Tap any URL below to open it in a WebView with full features:",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sampleUrls) { (url, title) ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onOpenWebView(url, title) }
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = url,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            
            Button(
                onClick = onOpenManagerDemo,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Open WebView Manager Demo")
            }
            
            Button(
                onClick = onOpenExamples,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("WebView Navigation Examples")
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Text(
                text = "Features included:\n• File upload/download\n• Navigation controls\n• Security policies\n• Error handling\n• User interruption detection\n• Direct WebViewManager API",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
