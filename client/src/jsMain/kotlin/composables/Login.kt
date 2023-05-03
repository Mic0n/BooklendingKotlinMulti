package composables

import androidx.compose.runtime.*
import com.example.common.ApiClient
import composables.components.NotificationItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import models.User
import models.UserRequest
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.flex
import org.jetbrains.compose.web.css.maxWidth
import org.jetbrains.compose.web.dom.*

class Login {
    companion object {
        @Composable
        fun render(
            scope: CoroutineScope,
            apiClient: ApiClient,
            user: User,
            updateUser: (User) -> Unit,
            updateState: (String) -> Unit
        ) {
            if (user.userId != "0") {
                updateState("main")
            } else {
                val name = remember { mutableStateOf("") }
                val password = remember { mutableStateOf("") }
                var notification by remember { mutableStateOf(0) }

                if (notification != 0) {
                    NotificationItem.render("Anmeldung fehlgeschlagen", "Benutzername oder Passwort falsch", "bg-danger")
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
                            Text("Anmelden")
                        }
                        Div(attrs = {
                            classes("form-floating")
                        }) {
                            Input(type = InputType.Text, attrs = {
                                classes("form-control")
                                classes("bg-dark")
                                classes("text-bg-dark")
                                classes("mb-3")
                                id("nameInput")
                                placeholder("Benutzername")
                                value(name.value)
                                onInput { event -> name.value = event.value }
                            })

                            Label(forId = "nameInput") {
                                Text("Benutzername")
                            }
                        }
                        Div(attrs = {
                            classes("form-floating")
                        }) {
                            Input(type = InputType.Password, attrs = {
                                classes("form-control")
                                classes("bg-dark")
                                classes("text-bg-dark")
                                id("passwordInput")
                                placeholder("Passwort")
                                value(password.value)
                                onInput { event -> password.value = event.value }
                            })
                            Label(forId = "passwordInput") {
                                Text("Passwort")
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
                                style { flex(1) }
                                onClick { updateState("loginSelection") }
                            }) {
                                Text("Zurück")
                            }
                            Button(attrs = {
                                classes("btn")
                                classes("btn-outline-light")
                                classes("mt-3")
                                if (name.value == "" || password.value == "") {
                                    disabled()
                                }
                                style { flex(1) }
                                onClick {
                                    scope.launch {
                                        updateUser(apiClient.loginUser(UserRequest(name.value, password.value)))
                                        if (user.userId == "0") {
                                            notification += 5
                                            kotlinx.browser.window.setTimeout({ notification -= 5 }, 5000)
                                        }
                                    }
                                }
                            }) {
                                Text("Anmelden")
                            }
                        }
                    }
                }
            }
        }
    }
}



