package composables

import androidx.compose.runtime.*
import com.example.common.ApiClient
import composables.components.InformationCard
import models.Book
import models.User
import io.ktor.util.date.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import models.LendRequest
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

class BorrowedBooks {
    companion object {
        @Composable
        fun render(
            scope: CoroutineScope, apiClient: ApiClient, user: User
        ) {
            var books by remember { mutableStateOf(listOf<Book>()) }
            var notification by remember { mutableStateOf(0) }

            scope.launch { books = user.token?.let { apiClient.getOwned(user) }!! }

            if (notification != 0) {
                Div(attrs = {
                    classes("position-fixed")
                    classes("m-3")
                    classes("p-3")
                    classes("card")
                    classes("bg-success")
                    classes("end-0")
                    classes("shadow-custom")
                    classes("z-index-5000")
                }) {

                    Div(attrs = {
                        classes("fw-bold")
                    }) { Text("Erfolg") }
                    Div(attrs = {}) {
                        Text("Buch wurde zurückgegeben")
                    }

                }
            }

            Div(attrs = {
                classes("container-xxl")
            }) {

                if (books.isEmpty()) {
                    InformationCard.render(
                        "Hinweis", "Zur Zeit hast du keine ausgeliehenen Bücher"
                    )
                } else {
                    for (book in books) {
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
                                        val returnDate = GMTDate(book.returnDate.toLong())
                                        Text("Rückgabefrist: ${returnDate.dayOfMonth}.${returnDate.month.ordinal + 1}.${returnDate.year}")
                                    }
                                }
                                Div(attrs = {
                                    classes("d-flex")
                                }) {
                                    Button(attrs = {
                                        classes("btn")
                                        classes("btn-outline-light")
                                        classes("my-auto")
                                        onClick {
                                            scope.launch {
                                                notification += 5
                                                kotlinx.browser.window.setTimeout({ notification -= 5 }, 5000)
                                                apiClient.giveBack(
                                                    LendRequest(user.userId, book.bookId, ""), user.token!!
                                                )
                                                books = apiClient.getOwned(user)
                                            }
                                        }
                                    }) {
                                        Text("Zurückgeben")
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



