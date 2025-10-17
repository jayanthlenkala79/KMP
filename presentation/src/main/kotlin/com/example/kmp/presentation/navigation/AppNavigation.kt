package com.example.kmp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.kmp.presentation.screens.HomeScreen
import com.example.kmp.presentation.screens.WebViewScreen
import com.example.kmp.presentation.screens.WebViewManagerDemo
import com.example.kmp.presentation.screens.WelcomeScreen
import com.example.kmp.presentation.screens.WebViewOptionsScreen
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * Main navigation setup for the app
 */
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        composable("welcome") {
            WelcomeScreen(
                onNavigateToWebView = { url, title ->
                    val encodedUrl = URLEncoder.encode(url, "UTF-8")
                    val encodedTitle = URLEncoder.encode(title, "UTF-8")
                    navController.navigate("webview?url=$encodedUrl&title=$encodedTitle")
                },
                onNavigateToHome = {
                    navController.navigate("home")
                },
                onNavigateToOptions = {
                    navController.navigate("webview-options")
                }
            )
        }
        
        composable("webview-options") {
            WebViewOptionsScreen(
                onNavigateToWebView = { url, title ->
                    val encodedUrl = URLEncoder.encode(url, "UTF-8")
                    val encodedTitle = URLEncoder.encode(title, "UTF-8")
                    navController.navigate("webview?url=$encodedUrl&title=$encodedTitle")
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("home") {
            HomeScreen(
                onOpenWebView = { url, title ->
                    val encodedUrl = URLEncoder.encode(url, "UTF-8")
                    val encodedTitle = URLEncoder.encode(title, "UTF-8")
                    navController.navigate("webview?url=$encodedUrl&title=$encodedTitle")
                },
                onOpenManagerDemo = {
                    navController.navigate("manager-demo")
                },
            )
        }
        
        composable("manager-demo") {
            WebViewManagerDemo()
        }
        
        
        composable(
            route = "webview?url={url}&title={title}",
            arguments = listOf(
                navArgument("url") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url")?.let { URLDecoder.decode(it, "UTF-8") } ?: ""
            val title = backStackEntry.arguments?.getString("title")?.let { URLDecoder.decode(it, "UTF-8") } ?: "Web"
            
            WebViewScreen(
                title = title,
                startUrl = url,
                onClose = { navController.popBackStack() }
            )
        }
    }
}
