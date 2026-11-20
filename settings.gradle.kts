pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        id("com.android.application") version "8.9.1" apply false
        id("com.android.library") version "8.9.1" apply false
        id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "LuPeFit"

include(":app")
include(":core:core-domain")
include(":core:core-ui")
include(":core:core-network")
include(":core:core-sync")