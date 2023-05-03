package de.hsflensburg.bookmanager.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm

import java.util.*

class JwtAuth private constructor(id: String) {
    private val algorithm = Algorithm.HMAC512(secret)


    val verifier: JWTVerifier = JWT.require(algorithm).withIssuer(issuer).withAudience(audience).build()

    fun getToken(id: String): String =
        JWT.create().withSubject("Authentication").withIssuer(issuer).withAudience(audience).withClaim("id", id)
            .withExpiresAt(getExpiration())
            .sign(algorithm)

    private fun getExpiration() = Date(System.currentTimeMillis() + validTil)

    companion object {
        private const val secret = "verySecretSecret"
        private const val issuer = "bookManager"
        private const val audience = "bookManager"
        private const val validTil = 99999999999999
        const val CLAIM = "id"


        lateinit var instance: JwtAuth
            private set

        fun initialize(id: String) {
            synchronized(this) {
                if (!this::instance.isInitialized) {
                    instance = JwtAuth(id)
                }
            }
        }
    }
}


