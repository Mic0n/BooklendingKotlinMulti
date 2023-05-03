plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    id("com.android.library")
}

repositories {
    mavenCentral()
}

group = "com.example"
version = "1.0-SNAPSHOT"

kotlin {
    android()
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }
    js(IR) {
        browser()
        binaries.executable()
    }


    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                implementation(project(":core"))
                implementation(kotlin("reflect"))
                implementation("io.ktor:ktor-client-auth:${extra["ktor.version"]}")
                implementation("io.ktor:ktor-client-core:${extra["ktor.version"]}")
                implementation("io.ktor:ktor-client-content-negotiation:${extra["ktor.version"]}")
                implementation("io.ktor:ktor-serialization-kotlinx-json:${extra["ktor.version"]}")
            }
        }

        val androidMain by getting {
            dependencies {
                api("androidx.appcompat:appcompat:1.6.0")
                api("androidx.core:core-ktx:1.9.0")

                implementation("io.ktor:ktor-client-cio:${extra["ktor.version"]}")

            }
        }

        val desktopMain by getting {
            dependencies {
                api(compose.preview)

                implementation("io.ktor:ktor-client-cio:${extra["ktor.version"]}")
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(compose.web.core)
                implementation(compose.runtime)
            }
        }
    }
}

android {
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }
    compileOptions {

    }
    namespace = "com.example.common"
}
dependencies {
    implementation(project(":core"))
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.0-alpha05")
    implementation("androidx.transition:transition-ktx:1.4.1")
    implementation("androidx.compose.material:material-icons-core:1.3.1")
    implementation("androidx.compose.material:material-icons-extended:1.3.1")

}
