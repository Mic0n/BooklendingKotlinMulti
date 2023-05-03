package com.example.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.common.jvmViews.*
import kotlinx.coroutines.launch
import models.User

@Composable
fun App() {
    val scaffoldState = rememberScaffoldState()
    val apiClient = ApiClient()
    val emptyUser = User("0", "0", "", isBanned = false, isAdmin = false, "0")
    val scope = rememberCoroutineScope()
    var currentUser by remember { mutableStateOf(emptyUser) }
    var state by remember { mutableStateOf("loginSelection") }
    val columnState = rememberScrollState()

    MaterialTheme(
        colors = darkColors()
    ) {
        Column(Modifier.fillMaxHeight().fillMaxWidth()) {

            /** TopAppBar */
            Row {
                TopAppBar(
                    Modifier.fillMaxWidth(), elevation = 12.dp
                ) {
                    if (currentUser.userId != "0") {
                        IconButton(onClick = {
                            scope.launch {
                                scaffoldState.drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
                    Text("Book Manager", textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))
                }
            }

            Row {
                /** Drawer */
                Scaffold(scaffoldState = scaffoldState, drawerGesturesEnabled = false, drawerContent = {
                    Column(
                        modifier = Modifier.fillMaxHeight().fillMaxWidth().verticalScroll(state = columnState),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            Text(currentUser.username, modifier = Modifier.padding(16.dp))
                        }
                        Divider()
                        Row {
                            addNavigationElement("main",
                                "Hauptmenü",
                                Icons.Default.Home,
                                scope,
                                scaffoldState,
                                updateState = { newState -> state = newState })
                        }
                        Row {
                            addNavigationElement("borrowedBooks",
                                "Ausgeliehene Bücher",
                                Icons.Default.ArrowForward,
                                scope,
                                scaffoldState,
                                updateState = { newState -> state = newState })
                        }
                        Row {
                            addNavigationElement("ownBooks",
                                "Meine Bücher",
                                Icons.Default.AccountBox,
                                scope,
                                scaffoldState,
                                updateState = { newState -> state = newState })
                        }
                        Row {
                            addNavigationElement("addBook",
                                "Buch Hinzufügen",
                                Icons.Default.Add,
                                scope,
                                scaffoldState,
                                updateState = { newState -> state = newState })
                        }
                        if (currentUser.isAdmin) {
                            addNavigationElement("admin",
                                "Admin",
                                Icons.Default.Edit,
                                scope,
                                scaffoldState,
                                updateState = { newState -> state = newState })
                        }
                        Row(modifier = Modifier.weight(1f, fill = true)) { }
                        Divider()
                        Row {
                            TextButton(
                                onClick = {
                                    println("Logout!")
                                    currentUser = emptyUser
                                    state = "loginSelection"
                                    scope.launch {
                                        scaffoldState.drawerState.apply {
                                            close()
                                        }
                                    }
                                },
                                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp).fillMaxWidth(),
                            ) {
                                Icon(Icons.Default.ExitToApp, contentDescription = "Abmelden")
                                Text("Abmelden", modifier = Modifier.padding(start = 16.dp).fillMaxWidth())
                            }
                        }
                    }
                }) {
                    /** Content */
                    when (state) {
                        "admin" -> {
                            if (currentUser.isAdmin) {
                                renderAdmin(
                                    scope, apiClient, currentUser
                                )
                            } else {
                                state = "main"
                            }
                        }

                        "loginSelection" -> renderLoginSelection(updateState = { newState -> state = newState })

                        "register" -> renderRegistration(apiClient,
                            scope,
                            updateState = { newState -> state = newState },
                            updateCurrentUser = { user -> currentUser = user },
                            currentUser
                        )

                        "login" -> renderLogin(apiClient,
                            scope,
                            updateState = { newState -> state = newState },
                            updateCurrentUser = { user -> currentUser = user },
                            currentUser
                        )

                        "main" -> renderMain(
                            currentUser, scope, apiClient
                        )

                        "borrowedBooks" -> renderBorrowedBooks(
                            currentUser, scope, apiClient
                        )

                        "addBook" -> renderAddBook(currentUser,
                            scope,
                            apiClient,
                            updateState = { newState -> state = newState })

                        "ownBooks" -> renderOwnBooks(
                            currentUser, scope, apiClient
                        )
                    }
                }
            }
        }
    }
}