package de.hsflensburg.bookmanager.dao

import org.jetbrains.exposed.dao.id.UUIDTable

object Books : UUIDTable() {
    val title = varchar("title", 128)
    val author = varchar("author", 128)
    val isAvailable = bool("isAvailable")
    val returnDate = varchar("returnDate", 128)
}