package com.example.kmp.monitoring.di

import com.example.kmp.monitoring.ConsoleMonitoring
import com.example.kmp.monitoring.Monitoring
import org.koin.dsl.module

/**
 * Koin module for monitoring
 */
val monitoringModule = module {
    single<Monitoring> { ConsoleMonitoring() }
}
