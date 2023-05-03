package models

import kotlinx.serialization.Serializable

@Serializable
data class BookInsertRequest(val author: String, val title: String, val user:String)