package com.example.common



import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import models.*


class ApiClient {
    /** IMPORTANT: Configuration
     * 1. Set 'ip' to ipv4 address of host computer
     * 2. Set host network configuration to private home network
     */
    private val ip = "192.168.2.54:8080"
    private val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        install(Auth) {}
    }

    private val emptyUser = User("0", "0", "0", false, false, "0")
    private val emptyBook = Book("", "", "", false, "")

    suspend fun getBooks(token: String): List<Book>? {
        val response = client.get("http://$ip/books/all") {
            bearerAuth(token)
        }
        return if (response.status.isSuccess()) Json.decodeFromString<List<Book>>(response.body()) else null
    }

    suspend fun getBooksAvailable(token: String): List<Book>? {
        val response = client.get("http://$ip/books/available") {
            bearerAuth(token)
        }
        return if (response.status.isSuccess()) Json.decodeFromString<List<Book>>(response.body()) else null
    }

    suspend fun getOwned(user: User): List<Book> {
        val response = client.post("http://$ip/ownedBooks") {
            bearerAuth(user.token!!)
            contentType(ContentType.Application.Json)
            setBody(user)
        }
        return if (response.status.isSuccess()) Json.decodeFromString(response.body()) else emptyList()
    }

    suspend fun getTrueOwned(user: User): List<Book> {
        val response = client.post("http://$ip/trueOwnedBooks") {
            bearerAuth(user.token!!)
            contentType(ContentType.Application.Json)
            setBody(user)
        }
        return if (response.status.isSuccess()) Json.decodeFromString(response.body()) else emptyList()
    }

    suspend fun giveBack(lendRequest: LendRequest, token: String): Book {
        val response = client.post("http://$ip/giveBack") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(lendRequest)
        }
        return if (response.status.isSuccess()) Json.decodeFromString(response.body()) else emptyBook
    }

    suspend fun addBook(book: BookInsertRequest, token: String) {
        client.post("http://$ip/addBook") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(book)
        }
    }

    suspend fun addUser(userRequest: UserRequest): User {
        val response = client.post("http://$ip/addUser") {
            contentType(ContentType.Application.Json)
            setBody(userRequest)
        }
        return if (response.status.isSuccess()) Json.decodeFromString(response.body()) else emptyUser
    }

    suspend fun loginUser(userRequest: UserRequest): User {
        val response = client.post("http://$ip/loginUser") {
            contentType(ContentType.Application.Json)
            setBody(userRequest)
        }
        return if (response.status.isSuccess()) Json.decodeFromString(response.body()) else emptyUser
    }

    suspend fun lendBook(lendRequest: LendRequest, token: String): Book {
        val response = client.post("http://$ip/lendBook") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(lendRequest)
        }
        return if (response.status.isSuccess()) Json.decodeFromString(response.body()) else emptyBook
    }

    suspend fun getUsers(token: String): List<User>? {
        val response = client.get("http://$ip/users") {
            bearerAuth(token)
        }
        return if (response.status.isSuccess()) Json.decodeFromString<List<User>>(response.body()) else null
    }

    suspend fun deleteUser(user: User, token: String) {
        client.post("http://$ip/deleteUser") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(user)
        }
    }

    suspend fun deleteBook(book: Book, token: String) {
        client.post("http://$ip/deleteBook") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(book)
        }
    }

    suspend fun banUser(user: User, token: String) {
        client.post("http://$ip/banUser") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(user)
        }
    }

    suspend fun releaseUser(user: User, token: String) {
        client.post("http://$ip/releaseUser") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(user)
        }
    }

    suspend fun getLender(book: Book, token: String): String {
        return client.post("http://$ip/getLender") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(book)
        }.body()
    }
}