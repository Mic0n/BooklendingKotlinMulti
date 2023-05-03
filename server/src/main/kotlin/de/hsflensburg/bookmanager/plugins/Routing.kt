package de.hsflensburg.bookmanager.plugins


import de.hsflensburg.bookmanager.bookService.BookService
import de.hsflensburg.bookmanager.bookService.UserService
import de.hsflensburg.bookmanager.bookService.bindBooks
import de.hsflensburg.bookmanager.bookService.bindUsers
import de.hsflensburg.bookmanager.security.JwtAuth
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import models.*
import org.kodein.di.instance


fun Application.configureRouting() {

    val kodeinBook = bindBooks()
    val kodeinUser = bindUsers()
    val bookService by kodeinBook.instance<BookService>()
    val userService by kodeinUser.instance<UserService>()

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }

    routing {

        post("/loginUser") {
            val request = call.receive<UserRequest>()
            val user = userService.loginUser(request)
            var token = "0"
            var resUser = User("0", "ERROR", "", false, false)
            if (user.isNotEmpty()) {
                resUser = user.first()
                token = JwtAuth.instance.getToken(resUser.userId)
            }
            resUser.token = token
            call.respond(resUser)
        }

        post("/addUser") {
            val request = call.receive<UserRequest>()
            val user = userService.insertUser(request)
            val token = JwtAuth.instance.getToken(user.userId)
            user.token = token
            call.respond(user)
        }

        authenticate {

            get("/books/available") {
                call.respond(bookService.getAllAvailable())
            }

            get("/books/all") {
                call.respond(bookService.getAll())
            }

            post("/addBook") {
                val request = call.receive<BookInsertRequest>()
                println(request.author)
                call.respond(bookService.insert(request))
            }

            post("/lendBook") {
                val request = call.receive<LendRequest>()
                call.respond(bookService.lendBook(request))
            }

            post("/ownedBooks") {
                val request = call.receive<User>()
                println(request.username)
                call.respond(bookService.getOwned(request))
            }

            post("/giveBack") {
                val request = call.receive<LendRequest>()
                call.respond(bookService.giveBack(request))
            }

            get("/users") {
                call.respond(userService.getAll())
            }

            post("/deleteUser") {
                val request = call.receive<User>()
                userService.deleteUser(request)
            }

            post("/banUser") {
                val request = call.receive<User>()
                userService.banUser(request)
            }

            post("/deleteBook") {
                println("trying to delete")
                val request = call.receive<Book>()
                bookService.deleteBook(request)
            }
            post("/releaseUser") {
                val request = call.receive<User>()
                userService.releaseUser(request)
            }

            post("/deleteBook") {
                println("trying to delete")
                val request = call.receive<Book>()
                bookService.deleteBook(request)
            }

            post("/trueOwnedBooks") {
                val request = call.receive<User>()
                println(request.username)
                call.respond(bookService.getTrueOwned(request))
            }

            post("/getLender") {
                val request = call.receive<Book>()
                call.respond(userService.getLender(request))
            }
        }
    }
}
