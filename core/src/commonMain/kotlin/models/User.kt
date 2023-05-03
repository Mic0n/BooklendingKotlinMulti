package models

import kotlinx.serialization.Serializable

@Serializable
data class User(val userId: String, val username: String, val password:String, val isBanned: Boolean, val isAdmin: Boolean, var token: String? = null)
