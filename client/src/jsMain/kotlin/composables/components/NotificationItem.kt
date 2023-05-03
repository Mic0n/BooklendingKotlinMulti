package composables.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

class NotificationItem {
    companion object {
        @Composable
        fun render(title: String, message: String, background: String) {
            Div(attrs = {
                classes("position-fixed")
                classes("m-3")
                classes("p-3")
                classes("card")
                classes(background)
                classes("end-0")
                classes("shadow-custom")
                classes("z-index-5000")
            }) {
                Div(attrs = {
                    classes("fw-bold")
                }) { Text(title) }
                Div { Text(message) }
            }
        }

    }
}



