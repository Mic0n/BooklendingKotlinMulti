package composables

import androidx.compose.runtime.*
import com.example.common.ApiClient
import models.Book
import models.User
import composables.components.InformationCard
import composables.components.NotificationItem
import io.ktor.util.date.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import models.LendRequest
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.css.minWidth
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

class Main {
    companion object {
        @Composable
        fun render(
            scope: CoroutineScope, apiClient: ApiClient, user: User
        ) {
            var books by remember { mutableStateOf(listOf<Book>()) }
            var notification by remember { mutableStateOf(0) }

            scope.launch { books = user.token?.let { apiClient.getBooksAvailable(it) }!! }

            if (notification != 0) {
                NotificationItem.render("Erfolg", "Buch wurde ausgeliehen", "bg-success")
            }

            Div(attrs = {
                classes("container-xxl")
            }) {

                if (books.isEmpty()) {
                    InformationCard.render(
                        "Hinweis", "Aktuell stehen keine Bücher zum Ausleihen zur Verfügung"
                    )
                } else {
                    for (book in books) {
                        var days by remember { mutableStateOf(1) }
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
                                    classes("flex-grow-1")
                                }) {
                                    Div(attrs = {
                                        classes("fs-2")
                                    }) {
                                        Text(book.title)
                                    }
                                    Div(attrs = {
                                        classes("fs-5")
                                    }) {
                                        Text(book.author)
                                    }
                                }
                                Div(attrs = {
                                    classes("d-flex")
                                    classes("flex-column")
                                    classes("gap-3")
                                }) {
                                    Div(attrs = {
                                        classes("row")
                                        classes("m-0")
                                        style { minWidth("150px") }
                                    }) {
                                        Button(attrs = {
                                            classes("btn")
                                            classes("btn-outline-light")
                                            classes("col-4")
                                            onClick {
                                                if (days > 1) {
                                                    days--
                                                }
                                            }
                                        }) {
                                            Text("-")
                                        }
                                        Div(attrs = {
                                            classes("col-4")
                                            classes("d-flex")
                                            classes("justify-content-center")
                                            classes("align-items-center")
                                        }) { Text("$days") }
                                        Button(attrs = {
                                            classes("btn")
                                            classes("btn-outline-light")
                                            classes("col-4")
                                            onClick {
                                                if (days < 30) {
                                                    days++
                                                }
                                            }
                                        }) {
                                            Text("+")
                                        }
                                    }
                                    Button(attrs = {
                                        classes("btn")
                                        classes("btn-outline-light")
                                        classes("my-auto")
                                        if (user.isBanned) disabled()
                                        onClick {
                                            scope.launch {
                                                notification += 5
                                                kotlinx.browser.window.setTimeout({ notification -= 5 }, 5000)
                                                apiClient.lendBook(
                                                    LendRequest(
                                                        user.userId,
                                                        book.bookId,
                                                        GMTDate().plus(days * 86400000.toLong()).timestamp.toString()
                                                    ), user.token!!
                                                )
                                                days = 1
                                                books = apiClient.getBooksAvailable(user.token!!)!!
                                            }
                                        }
                                    }) {
                                        Text("Ausleihen")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



