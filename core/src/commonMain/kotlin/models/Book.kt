package models


import kotlinx.serialization.Serializable

@Serializable
data class Book(val bookId: String, val title: String, val author: String, val isAvailable: Boolean = true, var returnDate: String)

