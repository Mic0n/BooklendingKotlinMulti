import androidx.compose.runtime.*
import com.example.common.ApiClient
import composables.*
import models.User
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.renderComposable

fun main() {
    WebApp()
}

fun WebApp() {
    val apiClient = ApiClient()
    val emptyUser = User("0", "0", "", isBanned = false, isAdmin = false, "0")

    renderComposable(rootElementId = "root") {
        var state by remember { mutableStateOf("loginSelection") }
        var user by remember { mutableStateOf(emptyUser) }
        val scope = rememberCoroutineScope()

        Navigation.render(updateState = { newState -> state = newState },
            updateUser = { newUser -> user = newUser },
            user
        )

        Div(attrs = {
            classes("bg-secondary")
            classes("flex-grow-1")
            classes("d-flex")
        }) {
            when (state) {
                "loginSelection" -> LoginSelection.render { newState -> state = newState }
                "login" -> Login.render(scope = scope,
                    apiClient = apiClient,
                    user = user,
                    updateUser = { newUser -> user = newUser },
                    updateState = { newState -> state = newState })

                "register" -> Register.render(scope = scope,
                    apiClient = apiClient,
                    user = user,
                    updateUser = { newUser -> user = newUser },
                    updateState = { newState -> state = newState })

                "main" -> Main.render(
                    scope = scope, apiClient = apiClient, user = user
                )

                "admin" -> Admin.render(
                    scope = scope, apiClient = apiClient, user = user
                )

                "borrowedBooks" -> BorrowedBooks.render(
                    scope = scope, apiClient = apiClient, user = user
                )

                "ownBooks" -> OwnBooks.render(
                    scope = scope, apiClient = apiClient, user = user
                )

                "addBook" -> AddBook.render(
                    scope = scope, apiClient = apiClient, user = user
                )
            }
        }


    }
}
