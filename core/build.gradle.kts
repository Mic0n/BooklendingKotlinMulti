
plugins{
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin{
    js(IR){
        browser()
    }
    jvm()

    sourceSets{
        val commonMain by getting{
            dependencies {
                implementation("io.ktor:ktor-serialization-kotlinx-json:${extra["ktor.version"]}")
            }

        }
    }
}