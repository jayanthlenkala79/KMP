package com.example.kmp.android.di

import android.content.Context
import com.example.kmp.data.WebViewPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * App-level Koin module
 */
val appModule = module {
    single<WebViewPreferences> { WebViewPreferences(androidContext()) }
}
