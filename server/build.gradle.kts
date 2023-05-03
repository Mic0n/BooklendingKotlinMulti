val ktorVersion = extra["ktor.version"].toString()
val kotlinVersion = extra["kotlin.version"].toString()
val logbackVersion = extra["logback.version"].toString()
val exposedVersion = extra["exposed.version"].toString()
val h2Version = extra["h2.version"].toString()

plugins {
    application
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("io.ktor.plugin")
}

group = "de.hsflensburg.projekt-maximilian-and-michael"
version = "0.0.1"


application {
    mainClass.set("de.hsflensburg.bookmanager.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    var applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))
    implementation("io.ktor:ktor-server-auth:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-server-freemarker:$ktorVersion")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("com.h2database:h2:$h2Version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-server-cors:$ktorVersion")
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-server-auth-jvm:2.1.3")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:2.1.3")
    implementation ("org.kodein.di:kodein-di-framework-ktor-server-jvm:7.16.0")
    implementation("org.kodein.di:kodein-di-jvm:7.16.0")
    implementation("io.ktor:ktor-server-cors-jvm:2.2.2")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    implementation("io.ktor:ktor-server-cors:$ktorVersion")

}