package com.example.kmp.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Screen showing various WebView options with different URLs
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewOptionsScreen(
    onNavigateToWebView: (url: String, title: String) -> Unit,
    onBack: () -> Unit
) {
    val webViewOptions = listOf(
        WebViewOption(
            icon = Icons.Default.Code,
            title = "GitHub",
            description = "Explore open source projects",
            url = "https://github.com",
            color = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ),
        WebViewOption(
            icon = Icons.Default.Search,
            title = "Google",
            description = "Search the web",
            url = "https://www.google.com",
            color = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ),
        WebViewOption(
            icon = Icons.Default.Android,
            title = "Android Developers",
            description = "Official Android documentation",
            url = "https://developer.android.com",
            color = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
        ),
        WebViewOption(
            icon = Icons.Default.Web,
            title = "Example.com",
            description = "Test website for WebView features",
            url = "https://example.com",
            color = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ),
        WebViewOption(
            icon = Icons.Default.Article,
            title = "MDN Web Docs",
            description = "Web development documentation",
            url = "https://developer.mozilla.org",
            color = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ),
        WebViewOption(
            icon = Icons.Default.Star,
            title = "Stack Overflow",
            description = "Programming Q&A community",
            url = "https://stackoverflow.com",
            color = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        )
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("WebView Options") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Choose a website to open in WebView",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Each option demonstrates different WebView capabilities",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            
            items(webViewOptions) { option ->
                WebViewOptionCard(
                    option = option,
                    onClick = { 
                        onNavigateToWebView(option.url, option.title)
                    }
                )
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Features Available",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                        
                        val features = listOf(
                            "• File upload/download support",
                            "• JavaScript bridge communication",
                            "• Security policies and host allowlists",
                            "• User interruption detection",
                            "• Navigation controls",
                            "• Error handling and monitoring"
                        )
                        
                        features.forEach { feature ->
                            Text(
                                text = feature,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WebViewOptionCard(
    option: WebViewOption,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = option.color,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = option.icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = option.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = option.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = option.url,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            
            Icon(
                Icons.Default.ArrowForward,
                contentDescription = "Open",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

data class WebViewOption(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val url: String,
    val color: CardColors
)
