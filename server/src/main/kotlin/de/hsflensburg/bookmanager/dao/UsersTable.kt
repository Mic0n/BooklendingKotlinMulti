package de.hsflensburg.bookmanager.dao

import org.jetbrains.exposed.dao.id.UUIDTable

object Users : UUIDTable() {
    val username = varchar("username", 128)
    val password = varchar("password", 128)
    val isAdmin = bool("isAdmin")
    val isBanned = bool("isbanned")
}