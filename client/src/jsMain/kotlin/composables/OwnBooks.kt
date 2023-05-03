package composables

import androidx.compose.runtime.*
import com.example.common.ApiClient
import composables.components.InformationCard
import composables.components.NotificationItem
import io.ktor.util.date.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import models.Book
import models.User
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

class OwnBooks {
    companion object {
        @Composable
        fun render(
            scope: CoroutineScope, apiClient: ApiClient, user: User
        ) {
            var books by remember { mutableStateOf(listOf<Book>()) }
            var notification by remember { mutableStateOf(0) }

            scope.launch { books = user.token?.let { apiClient.getTrueOwned(user) }!! }

            if (notification != 0) {
                NotificationItem.render("Erfolg", "Buch wurde entfernt", "bg-success")
            }

            Div(attrs = {
                classes("container-xxl")
            }) {

                if (books.isEmpty()) {
                    InformationCard.render("Hinweis", "Zur Zeit hast du keine Bücher eingestellt")
                } else {
                    for (book in books) {
                        var lender by remember { mutableStateOf("") }
                        scope.launch { lender = user.token?.let { apiClient.getLender(book, it) }!! }

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
                                    Div(attrs = {
                                        classes("fs-5")
                                    }) {
                                        Text("Verliehen an: $lender")
                                    }
                                    if (book.returnDate != "0") {
                                        Div(attrs = {
                                            classes("fs-5")
                                        }) {
                                            val returnDate = GMTDate(book.returnDate.toLong())
                                            Text("Rückgabefrist: ${returnDate.dayOfMonth}.${returnDate.month.ordinal + 1}.${returnDate.year}")
                                        }
                                    }
                                }
                                Div(attrs = {
                                    classes("d-flex")
                                }) {
                                    Button(attrs = {
                                        classes("btn")
                                        classes("btn-outline-light")
                                        classes("my-auto")
                                        if (lender != "Nicht verliehen") {
                                            disabled()
                                        }
                                        onClick {
                                            scope.launch {
                                                notification += 5
                                                kotlinx.browser.window.setTimeout({ notification -= 5 }, 5000)
                                                apiClient.deleteBook(book, user.token!!)
                                                books = apiClient.getTrueOwned(user)!!
                                            }
                                        }
                                    }) {
                                        Text("Entfernen")
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



