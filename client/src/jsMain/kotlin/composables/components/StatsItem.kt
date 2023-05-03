package composables.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

class StatsItem {
    companion object {
        @Composable
        fun render(value: String, entity: String) {
            Div(attrs = {
                classes("col")
                classes("d-flex")
            }) {
                Div(attrs = {
                    classes("fs-1")
                }) { Text(value) }
                Div(attrs = {
                    classes("fs-4")
                    classes("mb-1")
                    classes("ms-2")
                    classes("mt-auto")
                }) { Text(entity) }
            }
        }
    }
}



