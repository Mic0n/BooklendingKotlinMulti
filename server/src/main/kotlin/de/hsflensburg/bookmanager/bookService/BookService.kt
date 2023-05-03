package de.hsflensburg.bookmanager.bookService

import de.hsflensburg.bookmanager.dao.*
import models.Book
import models.BookInsertRequest
import models.LendRequest
import models.User
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class BookService {
    fun getAll(): List<Book> = transaction {
        BookEntity.all().map {
            it.toDTO()
        }
    }

    fun getAllAvailable(): List<Book> = transaction {
        BookEntity.find {
            Books.isAvailable eq true
        }.map {
            it.toDTO()
        }
    }

    fun insert(bookInsert: BookInsertRequest): Book = transaction {
        println(bookInsert.author)
        val book = BookEntity.new {
            this.author = bookInsert.author
            this.title = bookInsert.title
            this.isAvailable = true
            this.returnDate = "0"
        }

        val user = UserEntity.findById(UUID.fromString(bookInsert.user))
        OwnerEntity.new {
            this.book = book
            this.user = user!!
            this.trueOwner = true
        }
        book.toDTO()
    }

    fun lendBook(lendRequest: LendRequest): Book = transaction {
        val book = BookEntity.findById(UUID.fromString(lendRequest.bookId))!!
        val user = UserEntity.findById(UUID.fromString(lendRequest.userId))!!
        var isOwner = false
        user.books.toList().forEach {
            if (!isOwner) {
                isOwner = it.book.id == book.id && it.trueOwner == true
            }
            println("in foreach $isOwner")
        }
        println("out foreach $isOwner")
        if (!isOwner) {
            OwnerEntity.new {
                this.trueOwner = false
                this.book = book
                this.user = user
            }
        }
        book.returnDate = lendRequest.date
        book.isAvailable = false
        book.toDTO()
    }

    fun giveBack(lendRequest: LendRequest): Book = transaction {
        val book = BookEntity.findById(UUID.fromString(lendRequest.bookId))!!
        val user = UserEntity.findById(UUID.fromString(lendRequest.userId))!!

        var isOwner = false
        user.books.toList().forEach {
            if (!isOwner) {
                isOwner = it.book.id == book.id && it.trueOwner == true
            }
        }
        println(isOwner)
        if (!isOwner) {
            OwnerEntity.find {
                Owners.user eq user.id and (Owners.Book eq book.id)
            }.toList().forEach {
                it.delete()
            }
        }
        println(book.title)
        book.returnDate = "0"
        book.isAvailable = true
        book.toDTO()
    }

    fun getOwned(user: User): List<Book> = transaction {
        var books = emptyList<Book>()

        OwnerEntity.find {
            Owners.user eq UUID.fromString(user.userId)
        }.map {
            if (it.trueOwner) {
                if (OwnerEntity.find {
                        Owners.Book eq it.book.id
                    }.toList().size > 1) {
                } else {
                    if (!it.book.isAvailable) {
                        books += it.book.toDTO()
                    }
                }
            } else {
                books += it.book.toDTO()
            }

        }
        books

    }

    fun getTrueOwned(user: User): List<Book> = transaction {
        OwnerEntity.find {
            Owners.user eq UUID.fromString(user.userId) and (Owners.trueOwner eq true)
        }.map {
            it.book.toDTO()
        }
    }

    fun deleteBook(book: Book) = transaction {
        OwnerEntity.find {
            Owners.Book eq UUID.fromString(book.bookId)
        }.map {
            it.delete()
        }
        BookEntity.find {
            Books.id eq UUID.fromString(book.bookId)
        }.map {
            it.delete()
        }
    }
}