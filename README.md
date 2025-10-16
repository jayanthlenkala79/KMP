# KMP WebView Feature

A comprehensive WebView implementation for Android using Clean MVVM architecture with Koin dependency injection.

## Features

- **Full WebView functionality** with file upload/download support
- **Security-first approach** with HTTPS-only navigation and host allowlists
- **User interruption detection** for SSL errors, navigation blocks, back press, etc.
- **Clean MVVM architecture** with proper separation of concerns
- **Koin dependency injection** for modular and testable code
- **Compose UI** with modern Material 3 design
- **Comprehensive event system** for monitoring and debugging
- **File handling** with native Android pickers and DownloadManager
- **JavaScript bridge** for native-web communication

## Architecture

The project follows Clean MVVM architecture with the following modules:

```
:domain          # Business logic and policies
:data            # Data layer (preferences, repositories)
:presentation    # UI layer with navigation
:monitoring      # Analytics and error tracking
:feature:webview # Complete WebView feature module
:app             # Main application module
```

## Quick Start

### 1. Basic Usage

```kotlin
// Inject WebViewManager
val webViewManager: WebViewManager = koinInject()

// Open a WebView with event handling
webViewManager.open(
    startUrl = "https://example.com",
    title = "My WebView",
    allowHosts = setOf("example.com", "cdn.example.com"),
    onEvent = { event ->
        when (event) {
            is WebEvent.PageReady -> println("Page loaded: ${event.url}")
            is WebEvent.DownloadRequested -> handleDownload(event.url, event.filename)
            is WebEvent.UploadRequested -> showFilePicker(event.acceptTypes, event.multiple)
            is WebEvent.Error -> showError(event.description)
            // ... handle other events
        }
    }
)

// Send commands to the WebView
webViewManager.send(WebCommand.PostMessage("hello", """{"message": "Hello from native!"}"""))
webViewManager.send(WebCommand.Reload)
```

### 2. Using the Compose Screen

```kotlin
@Composable
fun MyScreen() {
    WebViewScreenPublic(
        title = "My WebView",
        startUrl = "https://example.com",
        allowHosts = setOf("example.com"),
        onClose = { /* handle close */ }
    )
}
```

### 3. Navigation Integration

```kotlin
// In your NavHost
composable("webview?url={url}&title={title}") { backStackEntry ->
    val url = backStackEntry.arguments?.getString("url")!!
    val title = backStackEntry.arguments?.getString("title")!!
    
    WebViewScreen(
        title = title,
        startUrl = url,
        onClose = { navController.popBackStack() }
    )
}
```

## Events

The WebView emits various events that you can handle:

```kotlin
sealed interface WebEvent {
    data class PageReady(val url: String) : WebEvent
    data class Navigation(val url: String) : WebEvent
    data class JsMessage(val name: String, val payload: String) : WebEvent
    data class DownloadRequested(val url: String, val mime: String?, val filename: String?) : WebEvent
    data class UploadRequested(val acceptTypes: List<String>, val multiple: Boolean) : WebEvent
    data class Error(val code: Int?, val description: String) : WebEvent
    data class Interruption(val kind: Kind, val detail: String? = null) : WebEvent {
        enum class Kind { 
            SslError, 
            NetworkLost, 
            BackPressed, 
            NavigationBlocked, 
            UploadCanceled, 
            DownloadFailed 
        }
    }
}
```

## Commands

Send commands to control the WebView:

```kotlin
sealed interface WebCommand {
    data class LoadUrl(val url: String) : WebCommand
    data class PostMessage(val name: String, val json: String) : WebCommand
    data object GoBack : WebCommand
    data object Reload : WebCommand
}
```

## Security Features

- **HTTPS-only navigation** by default
- **Host allowlists** for controlled navigation
- **Mixed content blocking** for security
- **File access restrictions** 
- **JavaScript bridge** with validation
- **SSL error handling**

## File Handling

### Upload

When a file upload is requested:

```kotlin
webViewManager.open(
    startUrl = "https://example.com",
    onEvent = { event ->
        if (event is WebEvent.UploadRequested) {
            // Launch file picker
            if (event.multiple) {
                // Use OpenMultipleDocuments contract
            } else {
                // Use OpenDocument contract
            }
        }
    }
)
```

### Download

Downloads are automatically handled using Android's DownloadManager:

```kotlin
// Downloads are automatically enqueued when WebEvent.DownloadRequested is emitted
// Files are saved to Downloads directory with proper naming
```

## Monitoring

The WebView integrates with monitoring for analytics and error tracking:

```kotlin
// Events are automatically traced:
// - web_page_ready
// - web_navigation  
// - web_download_requested
// - web_upload_requested
// - web_error
// - web_interruption
// - web_js_message
```

## Configuration

### Security Policy

Modify `WebViewPolicy` in the domain module:

```kotlin
object WebViewPolicy {
    val DEFAULT_ALLOWED_HOSTS = setOf("app.example.com", "static.example.com")
    
    object Security {
        const val REQUIRE_HTTPS = true
        const val BLOCK_MIXED_CONTENT = true
        const val ENABLE_JAVASCRIPT = true
    }
}
```

### Custom Event Handling

```kotlin
webViewManager.open(
    startUrl = url,
    onEvent = { event ->
        when (event) {
            is WebEvent.PageReady -> {
                // Page loaded successfully
                analytics.track("web_page_loaded", mapOf("url" to event.url))
            }
            is WebEvent.Error -> {
                // Handle errors
                crashlytics.recordException(WebViewException(event.description))
            }
            is WebEvent.Interruption -> {
                // Handle user interruptions
                when (event.kind) {
                    WebEvent.Interruption.Kind.SslError -> showSecurityWarning()
                    WebEvent.Interruption.Kind.NavigationBlocked -> showBlockedMessage()
                    // ... other cases
                }
            }
        }
    }
)
```

## JavaScript Bridge

The WebView exposes a JavaScript interface for native communication:

```javascript
// In your web page
window.Android.onJsMessage("eventName", JSON.stringify({data: "value"}));
```

```kotlin
// Handle in your app
webViewManager.open(
    onEvent = { event ->
        if (event is WebEvent.JsMessage) {
            when (event.name) {
                "eventName" -> {
                    val data = JSON.parseObject(event.payload)
                    // Handle the data
                }
            }
        }
    }
)
```

## Testing

The modular architecture makes testing easy:

```kotlin
// Test the WebViewManager
class WebViewManagerTest {
    @Test
    fun `should emit events correctly`() {
        val manager = WebViewManagerImpl(mockNavController)
        // Test event emission
    }
}

// Test the controller
class WebControllerTest {
    @Test
    fun `should handle commands correctly`() {
        val controller = WebController()
        // Test command handling
    }
}
```

## Dependencies

The project uses modern Android libraries:

- **Kotlin 2.0.21**
- **Compose BOM 2024.10.00**
- **Navigation Compose 2.8.4**
- **Koin 3.5.6**
- **WebKit 1.12.1**

## Sample Usage

The app includes a complete demo showing:

1. **Basic WebView usage** with navigation
2. **Direct WebViewManager API** usage
3. **Event handling** examples
4. **Command sending** examples
5. **File upload/download** demonstrations

Run the app and explore both the navigation-based WebView and the direct manager API to see all features in action.

## License

This project is part of a KMP (Kotlin Multiplatform) demonstration and follows standard open-source licensing.
