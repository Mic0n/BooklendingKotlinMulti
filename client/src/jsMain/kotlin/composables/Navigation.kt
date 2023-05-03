package composables

import androidx.compose.runtime.Composable
import models.User
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.I
import org.jetbrains.compose.web.dom.Text

class Navigation {
    companion object {
        @Composable
        fun render(updateState: (String) -> Unit, updateUser: (User) -> Unit, user: User) {
            if (user.userId != "0") {
                Div(attrs = {
                    classes("bg-dark")
                }) {
                    Div(attrs = {
                        classes("p-2")
                        classes("d-flex")
                        classes("flex-column")
                        classes("border-end")
                        classes("h-100")
                    }) {
                        Div(attrs = {
                            classes("fs-2")
                            classes("ms-2")
                        }) {
                            Text(user.username)
                        }
                        Div(attrs = {
                            classes("border-bottom")
                            classes("mt-2")
                        })

                        Button(attrs = {
                            classes("btn")
                            classes("btn-dark")
                            classes("mt-2")
                            classes("text-start")
                            onClick { updateState("main") }
                        }) {
                            I(attrs = {
                                classes("bi")
                                classes("bi-house")
                                classes("me-2")
                            })
                            Text("Hauptmenü")
                        }

                        Button(attrs = {
                            classes("btn")
                            classes("btn-dark")
                            classes("mt-2")
                            classes("text-start")
                            onClick { updateState("borrowedBooks") }
                        }) {
                            I(attrs = {
                                classes("bi")
                                classes("bi-journal-arrow-up")
                                classes("me-2")
                            })
                            Text("Ausgeliehene Bücher")
                        }

                        Button(attrs = {
                            classes("btn")
                            classes("btn-dark")
                            classes("mt-2")
                            classes("text-start")
                            onClick { updateState("ownBooks") }
                        }) {
                            I(attrs = {
                                classes("bi")
                                classes("bi-book")
                                classes("me-2")
                            })
                            Text("Meine Bücher")
                        }

                        Button(attrs = {
                            classes("btn")
                            classes("btn-dark")
                            classes("mt-2")
                            classes("text-start")
                            onClick { updateState("addBook") }
                        }) {
                            I(attrs = {
                                classes("bi")
                                classes("bi-plus-square")
                                classes("me-2")
                            })
                            Text("Buch hinzufügen")
                        }
                        println(user.isAdmin)
                        if (user.isAdmin) {
                            Button(attrs = {
                                classes("btn")
                                classes("btn-dark")
                                classes("mt-2")
                                classes("text-start")
                                onClick { updateState("admin") }
                            }) {
                                I(attrs = {
                                    classes("bi")
                                    classes("bi-shield-lock")
                                    classes("me-2")
                                })
                                Text("Admin")
                            }
                        }

                        Div(attrs = {
                            classes("border-bottom")
                            classes("mt-auto")
                        })

                        Button(attrs = {
                            classes("btn")
                            classes("btn-dark")
                            classes("mt-2")
                            classes("text-start")
                            onClick {
                                updateUser(User("0", "0", "", isBanned = false, isAdmin = false, "0"))
                                updateState("loginSelection")
                            }
                        }) {
                            I(attrs = {
                                classes("bi")
                                classes("bi-box-arrow-right")
                                classes("me-2")
                            })
                            Text("Abmelden")
                        }
                    }
                }
            }
        }
    }
}

