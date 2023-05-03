allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    kotlin("multiplatform") apply false
    kotlin("jvm") apply false
    kotlin("android") apply false version "1.7.20"
    kotlin("js") apply false version "1.7.20"
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("org.jetbrains.compose") apply false
}

tasks.whenTaskAdded {
    if(this.name.contains(":android:compileDebugJavaWithJavac")) {
        this.enabled = false
    }
    if(this.name.contains("wrapper")) {
        this.enabled = false
    }
}