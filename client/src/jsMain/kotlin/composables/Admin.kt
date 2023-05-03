package composables

import androidx.compose.runtime.*
import com.example.common.ApiClient
import composables.components.InformationCard
import composables.components.NotificationItem
import composables.components.StatsItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import models.Book
import models.User
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.*

class Admin {
    companion object {
        @Composable
        fun render(
            scope: CoroutineScope, apiClient: ApiClient, user: User
        ) {
            var users by remember { mutableStateOf(listOf<User>()) }
            var books by remember { mutableStateOf(listOf<Book>()) }
            var notification by remember { mutableStateOf(0) }
            var notificationText by remember { mutableStateOf("") }
            var selection by remember { mutableStateOf("users") }

            scope.launch { users = apiClient.getUsers(user.token!!)!! }
            scope.launch { books = apiClient.getBooks(user.token!!)!! }

            if (notification != 0) {
                NotificationItem.render("Erfolg", notificationText, "bg-success")
            }

            Div(attrs = {
                classes("container-xxl")
            }) {

                /* Stats */
                Div(attrs = {
                    classes("row")
                    classes("m-3")
                }) {
                    Div(attrs = {
                        classes("card")
                        classes("bg-dark")
                        classes("p-3")
                        classes("d-flex")
                        classes("flex-row")
                    }) {
                        Div(attrs = {
                            classes("d-flex")
                            classes("flex-grow-1")
                        }) {
                            StatsItem.render(users.size.toString(), "Nutzer")
                            StatsItem.render(books.size.toString(), "Bücher gesamt")
                            StatsItem.render(books.filter { it.isAvailable }.size.toString(), "Bücher verfügbar")
                            StatsItem.render(books.filter { !it.isAvailable }.size.toString(), "Bücher verliehen")
                        }
                    }
                }

                /* Selection */
                Div(attrs = {
                    classes("row")
                    classes("m-3")
                }) {
                    Div(attrs = {
                        classes("btn-group")
                        classes("p-0")
                    }) {
                        Input(type = InputType.Radio, attrs = {
                            classes("btn-check")
                            id("user-selection")
                            value("users")
                            if (selection == "users") {
                                checked(true)
                            } else {
                                checked(false)
                            }
                            onInput { selection = "users" }
                        })
                        Label(forId = "user-selection", attrs = {
                            classes("btn")
                            classes("btn-outline-dark")
                            classes("fw-bold")
                        }) {
                            Text("Nutzer")
                        }
                        Input(type = InputType.Radio, attrs = {
                            classes("btn-check")
                            id("book-selection")
                            value("users")
                            if (selection == "books") {
                                checked(true)
                            } else {
                                checked(false)
                            }
                            onInput { selection = "books" }
                        })
                        Label(forId = "book-selection", attrs = {
                            classes("btn")
                            classes("btn-outline-dark")
                            classes("fw-bold")
                        }) {
                            Text("Bücher")
                        }
                    }
                }

                var list: List<Any> = listOf()

                if (selection == "users") {
                    list = users
                } else if (selection == "books") {
                    list = books
                }

                if (list.isEmpty()) {
                    InformationCard.render("Hinweis", "Keine Einträge vorhanden")
                } else {
                    Div(attrs = {
                        classes("row")
                        classes("m-3")
                    }) {
                        Div(attrs = {
                            classes("card")
                            classes("bg-dark")
                            classes("p-3")
                            classes("d-flex")
                            classes("gap-3")
                        }) {
                            if (list == users) {
                                for (item in users) {
                                    Div(attrs = {
                                        classes("row")
                                    }) {
                                        Div(attrs = {
                                            classes("col")
                                            classes("d-flex")
                                            classes("flex-column")
                                            classes("my-auto")
                                        }) {
                                            Div(attrs = {
                                                classes("fs-2")
                                                if (item.isBanned) {
                                                    classes("text-danger")
                                                }
                                            }) {
                                                if (item.isBanned) {
                                                    Text("${item.username} (Gesperrt)")
                                                } else {
                                                    Text(item.username)
                                                }
                                            }
                                            Div(attrs = {
                                                classes("fs-5")
                                            }) {
                                                Text(item.userId)
                                            }
                                        }
                                        Div(attrs = {
                                            classes("col-auto")
                                            classes("d-flex")
                                            classes("flex-column")
                                            classes("gap-3")
                                        }) {
                                            Button(attrs = {
                                                classes("btn")
                                                classes("btn-outline-light")
                                                classes("my-auto")
                                                if (item.isBanned) {
                                                    onClick {
                                                        scope.launch {
                                                            notification += 5
                                                            notificationText = "Nutzer ${item.username} wurde entsperrt"
                                                            kotlinx.browser.window.setTimeout(
                                                                { notification -= 5 }, 5000
                                                            )
                                                            apiClient.releaseUser(item, user.token!!)
                                                            users = apiClient.getUsers(user.token!!)!!
                                                        }
                                                    }
                                                } else {
                                                    onClick {
                                                        scope.launch {
                                                            notification += 5
                                                            notificationText = "Nutzer ${item.username} wurde gesperrt"
                                                            kotlinx.browser.window.setTimeout(
                                                                { notification -= 5 }, 5000
                                                            )
                                                            apiClient.banUser(item, user.token!!)
                                                            users = apiClient.getUsers(user.token!!)!!
                                                        }
                                                    }
                                                }

                                            }) {
                                                if (item.isBanned) {
                                                    Text("Entsperren")
                                                } else {
                                                    Text("Sperren")
                                                }
                                            }
                                            Button(attrs = {
                                                classes("btn")
                                                classes("btn-outline-light")
                                                classes("my-auto")
                                                onClick {
                                                    scope.launch {
                                                        notification += 5
                                                        notificationText = "Nutzer ${item.username} wurde entfernt"
                                                        kotlinx.browser.window.setTimeout(
                                                            { notification -= 5 }, 5000
                                                        )
                                                        apiClient.deleteUser(item, user.token!!)
                                                        users = apiClient.getUsers(user.token!!)!!
                                                    }
                                                }
                                            }) {
                                                Text("Entfernen")
                                            }
                                        }
                                    }
                                    Div(attrs = {
                                        classes("row")
                                        classes("border-top")
                                        classes("mx-1")
                                    })
                                }
                            } else if (list == books) {
                                for (item in books) {
                                    Div(attrs = {
                                        classes("row")
                                    }) {
                                        Div(attrs = {
                                            classes("col")
                                            classes("d-flex")
                                            classes("flex-column")
                                            classes("my-auto")
                                        }) {
                                            Div(attrs = {
                                                classes("fs-2")
                                            }) {
                                                Text(item.title)
                                            }
                                            Div(attrs = {
                                                classes("fs-5")
                                            }) {
                                                Text(item.author)
                                            }
                                        }
                                        Div(attrs = {
                                            classes("col-auto")
                                            classes("d-flex")
                                            classes("flex-column")
                                            classes("gap-3")
                                        }) {
                                            Button(attrs = {
                                                classes("btn")
                                                classes("btn-outline-light")
                                                classes("my-auto")
                                                onClick {
                                                    scope.launch {
                                                        notification += 5
                                                        notificationText = "Buch wurde entfernt"
                                                        kotlinx.browser.window.setTimeout(
                                                            { notification -= 5 }, 5000
                                                        )
                                                        apiClient.deleteBook(item, user.token!!)
                                                        books = apiClient.getBooks(user.token!!)!!
                                                    }
                                                }
                                            }) {
                                                Text("Entfernen")
                                            }
                                        }
                                    }
                                    Div(attrs = {
                                        classes("row")
                                        classes("border-top")
                                        classes("mx-1")
                                    })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



