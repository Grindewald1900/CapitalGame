plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
}

// domain/build.gradle.kts
android {
    namespace = "com.example.capital"
    compileSdk {
        version = release(36)
    }
    defaultConfig {
        minSdk = 24 // Example value, use the same as your other modules
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.junit)
}