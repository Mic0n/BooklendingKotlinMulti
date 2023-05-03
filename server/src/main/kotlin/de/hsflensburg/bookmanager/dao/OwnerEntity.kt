package de.hsflensburg.bookmanager.dao

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class OwnerEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<OwnerEntity>(Owners)

    var trueOwner by Owners.trueOwner
    var user by UserEntity referencedOn Owners.user
    var book by BookEntity referencedOn Owners.Book

}