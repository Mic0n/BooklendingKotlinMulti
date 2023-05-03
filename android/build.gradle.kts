plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("plugin.serialization")
    kotlin("android")
}

group "com.example"
version "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":client"))
    implementation("androidx.activity:activity-compose:1.6.1")
}

android {
//    compileSdkVersion(33)
    compileSdk = 33
    defaultConfig {
        applicationId = "com.example.android"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0-SNAPSHOT"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    namespace = "com.example.android"
}
