package de.hsflensburg.bookmanager.dao

import models.User
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserEntity>(Users)

    var isAdmin by Users.isAdmin
    var password by Users.password
    var username by Users.username
    var isBanned by Users.isBanned
    val books by OwnerEntity referrersOn Owners.user
}

fun UserEntity.toDTO(): User {
    return User(
        isAdmin = isAdmin, password = password, username = username, userId = id.toString(), isBanned = isBanned
    )
}

