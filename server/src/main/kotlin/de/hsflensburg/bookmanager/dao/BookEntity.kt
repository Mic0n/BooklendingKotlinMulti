package de.hsflensburg.bookmanager.dao

import models.Book
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class BookEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<BookEntity>(Books)

    var author by Books.author
    var title by Books.title
    var isAvailable by Books.isAvailable
    var returnDate by Books.returnDate
}

fun BookEntity.toDTO(): Book {
    return Book(
        bookId = id.toString(), title = title, author = author, isAvailable = isAvailable, returnDate = returnDate
    )
}