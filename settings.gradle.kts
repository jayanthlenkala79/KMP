enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}


rootProject.name = "KMP"
include(":androidApp")
include(":shared")
include(":domain")
include(":data")
include(":presentation")
include(":monitoring")
include(":feature:webview")
