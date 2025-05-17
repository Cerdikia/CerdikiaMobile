// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    id("com.google.devtools.ksp") version "2.1.20-2.0.1" apply false // Upgrade KSP version
    id("androidx.navigation.safeargs.kotlin") version "2.9.0" apply false // Upgrade Navigation Safe Args version
}

ext {
    set("kotlinVersion", "2.1.20") // Explicitly define Kotlin version
}