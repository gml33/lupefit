buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.9.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.21")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.6")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}