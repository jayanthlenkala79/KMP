package com.example.kmp.android

import android.app.Application
import com.example.kmp.android.di.appModule
import com.example.kmp.feature.webview.di.webViewModule
import com.example.kmp.monitoring.di.monitoringModule
import com.example.kmp.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Application class with Koin DI setup
 */
class KmpApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidContext(this@KmpApplication)
            modules(
                appModule,
                monitoringModule,
                webViewModule,
                // presentationModule will be added when NavController is available
            )
        }
    }
}
