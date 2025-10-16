package com.example.kmp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.kmp.Platform
import com.example.kmp.presentation.di.presentationModule
import com.example.kmp.presentation.navigation.AppNavigation
import org.koin.core.context.loadKoinModules


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Platform().logSystemInfo()
        
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppContent()
                }
            }
        }
    }
}

@Composable
fun AppContent() {
    val navController = rememberNavController()
    
    // Load presentation module with NavController
    LaunchedEffect(navController) {
        loadKoinModules(presentationModule(navController))
    }
    
    AppNavigation(navController = navController)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        Text("Hello, Android!")
    }
}
