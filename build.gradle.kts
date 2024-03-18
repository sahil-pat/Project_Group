buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
        // Change the AGP version to 8.2.2
        classpath ("com.android.tools.build:gradle:8.2.2")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Disable the application plugin and kotlin-android plugin for now
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
}
