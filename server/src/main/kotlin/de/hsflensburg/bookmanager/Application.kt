package de.hsflensburg.bookmanager


import de.hsflensburg.bookmanager.dao.DatabaseFactory
import de.hsflensburg.bookmanager.plugins.configureJwt
import de.hsflensburg.bookmanager.plugins.configureRouting
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()
    configureJwt()
    configureRouting()
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
    }
}
