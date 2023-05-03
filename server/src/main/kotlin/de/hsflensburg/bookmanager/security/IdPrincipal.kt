package de.hsflensburg.bookmanager.security

import io.ktor.server.auth.*

data class IdPrincipal (val id: String):Principal