package composables.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

class InformationCard {
    companion object {
        @Composable
        fun render(title: String, message: String) {
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
                            classes("flex-grow-1")
                        }) {
                            Div(attrs = {
                                classes("fs-2")
                            }) {
                                Text(title)
                            }
                            Div(attrs = {
                                classes("fs-5")
                            }) {
                                Text(message)
                            }
                        }
                    }
                }
            }
        }
    }
}



