package de.hsflensburg.bookmanager.security

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private const val SECRET = "1202398423"
private const val ALGORITHM = "HmacSHA1"
private val hmacKey = SecretKeySpec(SECRET.encodeToByteArray(), ALGORITHM)

fun hash(password: String): String {
    val hmac = Mac.getInstance(ALGORITHM)
    hmac.init(hmacKey)
    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}