package composables

import androidx.compose.runtime.*
import com.example.common.ApiClient
import composables.components.NotificationItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import models.BookInsertRequest
import models.User
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.flex
import org.jetbrains.compose.web.css.maxWidth
import org.jetbrains.compose.web.dom.*

class AddBook {
    companion object {
        @Composable
        fun render(
            scope: CoroutineScope,
            apiClient: ApiClient,
            user: User,
        ) {
            val title = remember { mutableStateOf("") }
            val author = remember { mutableStateOf("") }
            var notification by remember { mutableStateOf(0) }

            if (notification != 0) {
                NotificationItem.render("Buch hinzugefügt", "Das Buch wurde erfolgreich hinzugefügt", "bg-success")
            }

            Div(attrs = {
                classes("card")
                classes("bg-dark")
                classes("m-auto")
                classes("w-75")
                style { maxWidth("600px") }
            }) {
                Div(attrs = {
                    classes("card-body")
                    classes("d-flex")
                    classes("flex-column")
                }) {
                    Div(attrs = {
                        classes("card-title")
                        classes("fs-2")
                        classes("text-center")
                    }) {
                        Text("Buch hinzufügen")
                    }
                    Div(attrs = {
                        classes("form-floating")
                    }) {
                        Input(type = InputType.Text, attrs = {
                            classes("form-control")
                            classes("bg-dark")
                            classes("text-bg-dark")
                            classes("mb-3")
                            id("titleInput")
                            placeholder("Titel")
                            value(title.value)
                            onInput { event -> title.value = event.value }
                        })

                        Label(forId = "nameInput") {
                            Text("Titel")
                        }
                    }

                    Div(attrs = {
                        classes("form-floating")
                    }) {
                        Input(type = InputType.Text, attrs = {
                            classes("form-control")
                            classes("bg-dark")
                            classes("text-bg-dark")
                            id("authorInput")
                            placeholder("Autor")
                            value(author.value)
                            onInput { event -> author.value = event.value }
                        })
                        Label(forId = "authorInput") {
                            Text("Autor")
                        }
                    }
                    Div(attrs = {
                        classes("d-flex")
                        classes("gap-2")
                    }) {
                        Button(attrs = {
                            classes("btn")
                            classes("btn-outline-light")
                            classes("mt-3")
                            if (title.value == "" || author.value == "") {
                                disabled()
                            }
                            style { flex(1) }
                            onClick {
                                scope.launch {
                                    apiClient.addBook(
                                        BookInsertRequest(author.value, title.value, user.userId), user.token!!
                                    )
                                    notification += 5
                                    kotlinx.browser.window.setTimeout({ notification -= 5 }, 5000)
                                    author.value = ""
                                    title.value = ""
                                }
                            }
                        }) {
                            Text("Hinzufügen")
                        }
                    }
                }
            }
        }
    }
}



