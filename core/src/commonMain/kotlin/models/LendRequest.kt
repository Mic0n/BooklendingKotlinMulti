package models

import kotlinx.serialization.Serializable

@Serializable
data class LendRequest(val userId: String, val bookId: String, val date: String)