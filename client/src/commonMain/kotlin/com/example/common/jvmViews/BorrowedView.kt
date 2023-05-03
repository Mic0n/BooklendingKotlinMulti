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
import io.ktor.util.date.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import models.Book
import models.LendRequest
import models.User

@Composable
fun renderBorrowedBooks(
    currentUser: User, scope: CoroutineScope, apiClient: ApiClient
) {
    println("in MyBooks")
    var bookList by remember { mutableStateOf(listOf<Book>()) }
    val columnState = rememberScrollState()
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) {
        Column(modifier = Modifier.verticalScroll(state = columnState)) {
            scope.launch {
                bookList = apiClient.getOwned(currentUser)
            }
            if (bookList.isEmpty()) {
                Card(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp).fillMaxWidth(),
                    elevation = 16.dp
                ) {
                    Text("Du hast aktuelle keine Bücher ausgeliehen", modifier = Modifier.padding(16.dp))
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
                                    if (width < 600.dp) {
                                        val days = it.returnDate.toLong() - GMTDate().timestamp
                                        Text(
                                            "Noch " + GMTDate(days).dayOfYear + " Tage",
                                            modifier = Modifier.padding(start = 16.dp)
                                        )
                                        Row {
                                            Button(
                                                onClick = {
                                                    scope.launch {
                                                        apiClient.giveBack(
                                                            LendRequest(
                                                                currentUser.userId, it.bookId, ""
                                                            ), currentUser.token!!
                                                        )
                                                        bookList = apiClient.getOwned(currentUser)
                                                        scaffoldState.snackbarHostState.showSnackbar(
                                                            "Buch zurückgegeben",
                                                            actionLabel = "Ok",
                                                        )
                                                    }
                                                }, modifier = Modifier.padding(
                                                    start = 16.dp, top = 0.dp, bottom = 16.dp, end = 16.dp
                                                ).fillMaxWidth()
                                            ) {
                                                Text(text = "Zurückgeben")
                                            }
                                        }
                                    }
                                }
                                if (width >= 600.dp) {
                                    val days = it.returnDate.toLong() - GMTDate().timestamp
                                    Text(
                                        "Noch " + GMTDate(days).dayOfYear + " Tage",
                                        modifier = Modifier.padding(top = 16.dp)
                                    )
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
                                                        apiClient.giveBack(
                                                            LendRequest(
                                                                currentUser.userId, it.bookId,

                                                                ""
                                                            ), currentUser.token!!
                                                        )
                                                        bookList = apiClient.getOwned(currentUser)
                                                        scaffoldState.snackbarHostState.showSnackbar(
                                                            "Buch zurückgegeben",
                                                            actionLabel = "Ok",
                                                        )
                                                    }
                                                }) {
                                                    Text(text = "Zurückgeben")
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