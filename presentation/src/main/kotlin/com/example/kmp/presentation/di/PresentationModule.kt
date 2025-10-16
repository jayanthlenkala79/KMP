package com.example.kmp.presentation.di

import androidx.navigation.NavController
import org.koin.dsl.module

/**
 * Koin module for presentation layer
 */
fun presentationModule(navController: NavController) = module {
    single<NavController> { navController }
}
