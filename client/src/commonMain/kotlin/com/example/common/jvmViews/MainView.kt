package com.example.common.jvmViews

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
fun renderMain(
    currentUser: User,
    scope: CoroutineScope,
    apiClient: ApiClient
) {
    val columnState = rememberScrollState()
    val scaffoldState = rememberScaffoldState()
    var bookList by remember { mutableStateOf(listOf<Book>()) }

    Scaffold(scaffoldState = scaffoldState) {
        Column(modifier = Modifier.verticalScroll(state = columnState)) {
            if (currentUser.userId != "0") {
                if (currentUser.isAdmin) {
                    scope.launch {
                        bookList = apiClient.getBooks(currentUser.token!!)!!
                    }
                } else {
                    scope.launch {
                        bookList = apiClient.getBooksAvailable(currentUser.token!!)!!
                    }
                }
                if (bookList.isEmpty()) {
                    Card(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp).fillMaxWidth(),
                        elevation = 16.dp
                    ) {
                        Text(
                            "Aktuell stehen keine Bücher zum ausleihen zur Verfügung",
                            modifier = Modifier.padding(16.dp)
                        )
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
                                            if (!currentUser.isAdmin) {
                                                var days by remember { mutableStateOf(1) }
                                                Column(
                                                    modifier = Modifier.padding(
                                                        start = 16.dp, top = 8.dp, bottom = 8.dp, end = 16.dp
                                                    )
                                                ) {
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween
                                                    ) {
                                                        Button(
                                                            onClick = {
                                                                if (days > 1) {
                                                                    days--
                                                                } else {
                                                                    days = 1
                                                                }
                                                            }, modifier = Modifier.size(
                                                                height = 35.dp, width = 35.dp
                                                            )
                                                        ) { Text("-", textAlign = TextAlign.Center) }
                                                        Text("für $days Tage", textAlign = TextAlign.Center)
                                                        Button(
                                                            onClick = {
                                                                if (days < 30) {
                                                                    days++
                                                                } else {
                                                                    days = 30
                                                                }
                                                            }, modifier = Modifier.size(
                                                                height = 35.dp, width = 35.dp
                                                            )
                                                        ) { Text("+", textAlign = TextAlign.Center) }
                                                    }
                                                }
                                                Row {
                                                    Button(
                                                        onClick = {
                                                            scope.launch {
                                                                apiClient.lendBook(
                                                                    LendRequest(
                                                                        currentUser.userId,
                                                                        it.bookId,
                                                                        GMTDate().plus(days * 86400000.toLong()).timestamp.toString(),
                                                                    ), currentUser.token!!
                                                                )
                                                                days = 1
                                                                bookList =
                                                                    apiClient.getBooksAvailable(currentUser.token!!)!!
                                                                scaffoldState.snackbarHostState.showSnackbar(
                                                                    "Buch ausgeliehen",
                                                                    actionLabel = "Ok",
                                                                )
                                                            }
                                                        }, modifier = Modifier.padding(
                                                            start = 16.dp, top = 0.dp, bottom = 16.dp, end = 16.dp
                                                        ).fillMaxWidth()
                                                    ) { Text(text = "Ausleihen") }
                                                }
                                            } else {
                                                Row {
                                                    Button(
                                                        onClick = {
                                                            scope.launch {
                                                                apiClient.deleteBook(it, currentUser.token!!)
                                                                bookList = apiClient.getBooks(currentUser.token!!)!!
                                                                scaffoldState.snackbarHostState.showSnackbar(
                                                                    "Buch gelöscht",
                                                                    actionLabel = "Ok",
                                                                )
                                                            }
                                                        }, modifier = Modifier.padding(
                                                            start = 16.dp, top = 0.dp, bottom = 16.dp, end = 16.dp
                                                        ).fillMaxWidth()
                                                    ) { Text(text = "Löschen") }
                                                }
                                            }
                                        }
                                    }
                                    if (width >= 600.dp) {
                                        if (!currentUser.isAdmin) {
                                            var days by remember { mutableStateOf(1) }
                                            Column {
                                                Row(
                                                    modifier = Modifier.size(220.dp, 85.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Button(
                                                        onClick = {
                                                            if (days > 1) {
                                                                days--
                                                            } else {
                                                                days = 1
                                                            }
                                                        }, modifier = Modifier.size(
                                                            height = 35.dp, width = 35.dp
                                                        )
                                                    ) { Text("-") }
                                                    Text("für $days Tage", modifier = Modifier.padding(top = 16.dp))
                                                    Button(
                                                        onClick = {
                                                            if (days < 30) {
                                                                days++
                                                            } else {
                                                                days = 30
                                                            }
                                                        }, modifier = Modifier.size(
                                                            height = 35.dp, width = 35.dp
                                                        )

                                                    ) { Text("+") }
                                                }
                                            }
                                            Column {
                                                Row(
                                                    modifier = Modifier.fillMaxHeight().padding(18.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                ) {
                                                    var popupControl by remember { mutableStateOf(false) }
                                                    var book: Book
                                                    if (!currentUser.isBanned) {
                                                        Button(onClick = {
                                                            scope.launch {
                                                                println(days)
                                                                book = apiClient.lendBook(
                                                                    LendRequest(
                                                                        currentUser.userId,
                                                                        it.bookId,
                                                                        GMTDate().plus(days * 86400000.toLong()).timestamp.toString()
                                                                    ), currentUser.token!!
                                                                )
                                                                println(book.title)
                                                                days = 1
                                                                bookList =
                                                                    apiClient.getBooksAvailable(currentUser.token!!)!!
                                                                scaffoldState.snackbarHostState.showSnackbar(
                                                                    "Buch ausgeliehen",
                                                                    actionLabel = "Ok",
                                                                )
                                                            }
                                                            popupControl = true
                                                        }) { Text(text = "Ausleihen") }
                                                    }
                                                }
                                            }
                                        } else {
                                            Column(
                                                modifier = Modifier.fillMaxHeight(),
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxHeight().padding(18.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    var popupControl by remember { mutableStateOf(false) }
                                                    if (!currentUser.isBanned) {
                                                        Button(onClick = {
                                                            scope.launch {
                                                                apiClient.deleteBook(it, currentUser.token!!)
                                                                bookList = apiClient.getBooks(currentUser.token!!)!!
                                                                scaffoldState.snackbarHostState.showSnackbar(
                                                                    "Buch gelöscht",
                                                                    actionLabel = "Ok",
                                                                )
                                                            }
                                                            popupControl = true
                                                        }) { Text(text = "Löschen") }
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
}