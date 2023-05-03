package de.hsflensburg.bookmanager.dao

import org.jetbrains.exposed.dao.id.UUIDTable

object Owners : UUIDTable() {
    val trueOwner = bool("trueOwner")
    val user = reference("user", Users)
    val Book = reference("book", Books)
}