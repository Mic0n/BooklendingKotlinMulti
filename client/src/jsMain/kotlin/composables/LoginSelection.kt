package composables

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.maxWidth
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

class LoginSelection {
    companion object {
        @Composable
        fun render(updateState: (String) -> Unit) {
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
                        Text("Willkommen bei BookManager!")
                    }
                    Button(attrs = {
                        classes("btn")
                        classes("btn-outline-light")
                        classes("mt-2")
                        onClick { updateState("register") }
                    }) {
                        Text("Registrieren")
                    }
                    Button(attrs = {
                        classes("btn")
                        classes("btn-outline-light")
                        classes("mt-2")
                        onClick { updateState("login") }
                    }) {
                        Text("Anmelden")
                    }
                }
            }
        }
    }
}

