package de.hsflensburg.bookmanager.plugins

import de.hsflensburg.bookmanager.security.IdPrincipal
import de.hsflensburg.bookmanager.security.JwtAuth
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureJwt() {

    JwtAuth.initialize("geheim")
    authentication {
        jwt {
            verifier(JwtAuth.instance.verifier)
            validate {
                val claim = it.payload.getClaim(JwtAuth.CLAIM).asString()
                if (claim != null) {
                    IdPrincipal(claim)
                } else {
                    null
                }
            }
        }
    }
}