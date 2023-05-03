package de.hsflensburg.bookmanager.bookService

import de.hsflensburg.bookmanager.dao.*
import de.hsflensburg.bookmanager.security.hash
import models.Book
import models.User
import models.UserRequest
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class UserService {
    fun getAll(): List<User> = transaction {
        UserEntity.all().map {
            it.toDTO()
        }
    }

    fun banUser(user: User) = transaction {
        UserEntity[UUID.fromString(user.userId)].isBanned = true
    }

    fun releaseUser(user: User) = transaction {
        UserEntity[UUID.fromString(user.userId)].isBanned = false
    }

    fun deleteUser(user: User) = transaction {
        OwnerEntity.find {
            Owners.user eq UUID.fromString(user.userId)
        }.map {
            if (it.trueOwner) {
                OwnerEntity.find {
                    Owners.Book eq it.book.id
                }.forEach {
                    it.delete()
                }
                it.book.delete()
            } else {
                it.book.returnDate = "0"
                it.book.isAvailable = true
                it.delete()
            }
        }
        UserEntity[UUID.fromString(user.userId)].delete()
    }

    fun insertUser(userRequest: UserRequest): User = transaction {
        var user = User("0", "ERROR", "", false, false)
        if (UserEntity.find {
                Users.username eq userRequest.username
            }.toList().isEmpty()) {
            user = UserEntity.new {
                this.username = userRequest.username
                this.password = hash(userRequest.password)
                this.isAdmin = false
                this.isBanned = false
            }.toDTO()
        }
        user
    }

    fun loginUser(userRequest: UserRequest): List<User> = transaction {

        val password = hash(userRequest.password)
        UserEntity.find {
            Users.username eq userRequest.username and (Users.password eq password)
        }.map {
            println(it.username)
            it.toDTO()
        }
    }

    fun getLender(book: Book): String = transaction {
        var currentLender = ""
        if (book.isAvailable) {
            currentLender = "Nicht verliehen"
        } else {
            var ownerEntityList = emptyList<OwnerEntity>()
            OwnerEntity.find {
                Owners.Book eq UUID.fromString(book.bookId)
            }.map {
                ownerEntityList += it
            }
            if (ownerEntityList.size < 2) {
                currentLender = "Ihnen selbst"
            } else {
                ownerEntityList.forEach {
                    if (!it.trueOwner) {
                        currentLender = it.user.username
                    }
                }
            }
        }
        currentLender
    }
}