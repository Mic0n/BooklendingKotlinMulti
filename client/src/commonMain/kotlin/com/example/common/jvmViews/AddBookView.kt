package com.example.common.jvmViews

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import models.BookInsertRequest
import models.User

@Composable
fun renderAddBook(
    currentUser: User, scope: CoroutineScope, apiClient: ApiClient, updateState: (String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    val scaffoldState = rememberScaffoldState()
    val refresher = remember { mutableStateOf(false) }

    Scaffold(scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Buch hinzufügen",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    label = { Text("Titel") },
                    value = title,
                    singleLine = true,
                    onValueChange = { title = it },
                    modifier = Modifier.widthIn(0.dp, 400.dp).fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    label = { Text("Author") },
                    value = author,
                    onValueChange = { author = it },
                    singleLine = true,
                    modifier = Modifier.widthIn(0.dp, 400.dp).fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp)
                )
            }
            BoxWithConstraints {
                if (maxWidth < 400.dp) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = {
                                    updateState("main")
                                },
                                Modifier.widthIn(0.dp, 400.dp).fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp)
                            ) {
                                Text("Zurück")
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
                        ) {

                            Button(
                                onClick = {
                                    scope.launch {
                                        apiClient.addBook(
                                            BookInsertRequest(author, title, currentUser.userId), currentUser.token!!
                                        )
                                        title = ""
                                        author = ""
                                    }
                                    refresher.value = true
                                    scope.launch {
                                        val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                                            "Buch hinzugefügt",
                                            actionLabel = "Ok",
                                        )
                                        refresher.value = when (snackbarResult) {
                                            SnackbarResult.Dismissed -> false
                                            SnackbarResult.ActionPerformed -> false
                                        }
                                    }
                                },
                                modifier = Modifier.widthIn(0.dp, 400.dp).fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp),
                                enabled = title != "" && author != ""
                            ) {
                                Text(text = "Hinzufügen")
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                updateState("main")
                            },
                            Modifier.widthIn(0.dp, 200.dp).fillMaxWidth()
                                .padding(start = 16.dp, end = 4.dp, top = 4.dp, bottom = 4.dp)
                        ) {
                            Text("Zurück")
                        }
                        Button(
                            onClick = {
                                scope.launch {
                                    apiClient.addBook(
                                        BookInsertRequest(author, title, currentUser.userId), currentUser.token!!
                                    )
                                    title = ""
                                    author = ""
                                }
                                refresher.value = true
                                scope.launch {
                                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                                        "Buch hinzugefügt",
                                        actionLabel = "Ok",
                                    )
                                    refresher.value = when (snackbarResult) {
                                        SnackbarResult.Dismissed -> false
                                        SnackbarResult.ActionPerformed -> false
                                    }
                                }

                            },
                            modifier = Modifier.widthIn(0.dp, 200.dp).fillMaxWidth()
                                .padding(start = 4.dp, end = 16.dp, top = 4.dp, bottom = 4.dp),
                            enabled = title != "" && author != ""
                        ) {
                            Text(text = "Hinzufügen")
                        }
                    }
                }
            }
        }
    }
}