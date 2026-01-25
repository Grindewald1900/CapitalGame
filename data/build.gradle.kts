plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.devtools.ksp)
}

android {
    namespace = "com.example.capital"
    compileSdk {
        version = release(36)
    }
    defaultConfig {
        minSdk = 24
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
    implementation(project(":domain"))

    // Hilt (optional but common in data module)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Coroutines (common for repositories)
    implementation(libs.kotlinx.coroutines.core)
    //datastore
    implementation(libs.androidx.datastore.preferences)
    //room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
}