package com.example.kmp


actual class Platform {
    actual val osName: String
        get() = TODO("Not yet implemented")
    actual val osVersion: String
        get() = TODO("Not yet implemented")
    actual val deviceModel: String
        get() = TODO("Not yet implemented")
    actual val density: Int
        get() = TODO("Not yet implemented")

    actual fun logSystemInfo() {
    }

}