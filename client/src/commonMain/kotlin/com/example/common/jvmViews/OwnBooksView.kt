package com.example.common.jvmViews

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import models.Book
import models.User

@Composable
fun renderOwnBooks(
    currentUser: User, scope: CoroutineScope, apiClient: ApiClient
) {
    println("in MyBooks")
    var bookList by remember { mutableStateOf(listOf<Book>()) }
    val columnState = rememberScrollState()
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) {
        Column(modifier = Modifier.verticalScroll(state = columnState)) {
            scope.launch {
                bookList = apiClient.getTrueOwned(currentUser)
            }
            if (bookList.isEmpty()) {
                Card(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp).fillMaxWidth(),
                    elevation = 16.dp
                ) {
                    Text("Du hast noch keine Bücher hinzugefügt", modifier = Modifier.padding(16.dp))
                }
            } else {
                bookList.forEach {
                    Card(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp).fillMaxWidth(),
                        elevation = 16.dp
                    ) {
                        BoxWithConstraints {
                            val width = maxWidth
                            Row(
                                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                                    ) {
                                        Text(it.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Row(
                                        modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 16.dp)
                                    ) {
                                        Text(it.author)
                                    }
                                    var lender by remember { mutableStateOf("") }
                                    scope.launch {
                                        lender = apiClient.getLender(it, currentUser.token!!)
                                    }
                                    Row(
                                        modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 16.dp)
                                    ) {
                                        Text("Ausgeliehen von: $lender")
                                    }
                                    if (width < 400.dp) {
                                        Row {
                                            Button(
                                                onClick = {
                                                    scope.launch {
                                                        apiClient.deleteBook(it, currentUser.token!!)
                                                        bookList = apiClient.getTrueOwned(currentUser)
                                                        scaffoldState.snackbarHostState.showSnackbar(
                                                            "Buch entfernt",
                                                            actionLabel = "Ok",
                                                        )
                                                    }
                                                }, modifier = Modifier.padding(
                                                    start = 16.dp, top = 0.dp, bottom = 16.dp, end = 16.dp
                                                ).fillMaxWidth()
                                            ) {
                                                Text(text = "Entfernen")
                                            }
                                        }
                                    }
                                }

                                if (width >= 400.dp) {
                                    Column(
                                        modifier = Modifier.fillMaxHeight(),
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxHeight().padding(18.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            if (!currentUser.isBanned) {
                                                Button(onClick = {
                                                    scope.launch {
                                                        apiClient.deleteBook(it, currentUser.token!!)
                                                        bookList = apiClient.getTrueOwned(currentUser)
                                                        scaffoldState.snackbarHostState.showSnackbar(
                                                            "Buch entfernt",
                                                            actionLabel = "Ok",
                                                        )
                                                    }
                                                }) {
                                                    Text(text = "Entfernen")
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
            Row(
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Text("")
            }
        }
    }
}