package com.example.common.jvmViews

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import models.User

@Composable
fun renderAdmin(
    scope: CoroutineScope, apiClient: ApiClient, currentUser: User
) {
    println("in Admnin")
    var userList by remember { mutableStateOf(listOf<User>()) }
    val columnState = rememberScrollState()
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) {
        Column(modifier = Modifier.verticalScroll(state = columnState)) {
            scope.launch {
                userList = apiClient.getUsers(currentUser.token!!)!!
            }
            userList.forEach {
                if (!it.isAdmin) {
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
                                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
                                    ) {
                                        if (it.isBanned) {
                                            Text(
                                                "${it.username} (Gesperrt)",
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Red
                                            )
                                        } else {
                                            Text(it.username, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    Row(
                                        modifier = Modifier.padding(
                                            start = 16.dp, top = 8.dp, bottom = 16.dp, end = 16.dp
                                        )
                                    ) {
                                        Text(it.userId)
                                    }
                                    if (width < 400.dp) {
                                        Row {
                                            if (it.isBanned) {
                                                Button(
                                                    onClick = {
                                                        scope.launch {
                                                            apiClient.releaseUser(it, currentUser.token!!)
                                                            userList = apiClient.getUsers(currentUser.token!!)!!
                                                            scaffoldState.snackbarHostState.showSnackbar(
                                                                "Benutzer freigegeben",
                                                                actionLabel = "Ok",
                                                            )
                                                        }
                                                    }, modifier = Modifier.padding(
                                                        start = 16.dp, top = 0.dp, end = 16.dp
                                                    ).fillMaxWidth()
                                                ) {
                                                    Text(text = "Freigeben")
                                                }
                                            } else {
                                                Button(
                                                    onClick = {
                                                        scope.launch {
                                                            apiClient.banUser(it, currentUser.token!!)
                                                            userList = apiClient.getUsers(currentUser.token!!)!!
                                                            scaffoldState.snackbarHostState.showSnackbar(
                                                                "Benutzer gesperrt",
                                                                actionLabel = "Ok",
                                                            )
                                                        }
                                                    }, modifier = Modifier.padding(
                                                        start = 16.dp, top = 0.dp, end = 16.dp
                                                    ).fillMaxWidth()
                                                ) {
                                                    Text(text = "Sperren")
                                                }
                                            }
                                        }
                                        Row {
                                            Button(
                                                onClick = {
                                                    scope.launch {
                                                        apiClient.deleteUser(it, currentUser.token!!)
                                                        userList = apiClient.getUsers(currentUser.token!!)!!
                                                        scaffoldState.snackbarHostState.showSnackbar(
                                                            "Benutzer entfernt",
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
                                        modifier = Modifier.fillMaxHeight().padding(16.dp),
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxHeight(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            if (it.isBanned) {
                                                Button(onClick = {
                                                    scope.launch {
                                                        apiClient.releaseUser(it, currentUser.token!!)
                                                        userList = apiClient.getUsers(currentUser.token!!)!!
                                                        scaffoldState.snackbarHostState.showSnackbar(
                                                            "Benutzer freigegeben",
                                                            actionLabel = "Ok",
                                                        )
                                                    }
                                                }) {
                                                    Text(text = "Freigeben")
                                                }
                                            } else {
                                                Button(onClick = {
                                                    scope.launch {
                                                        apiClient.banUser(it, currentUser.token!!)
                                                        userList = apiClient.getUsers(currentUser.token!!)!!
                                                        scaffoldState.snackbarHostState.showSnackbar(
                                                            "Benutzer gesperrt",
                                                            actionLabel = "Ok",
                                                        )
                                                    }
                                                }) {
                                                    Text(text = "Sperren")
                                                }
                                            }
                                        }
                                        Row(
                                            modifier = Modifier.fillMaxHeight(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Button(onClick = {
                                                scope.launch {
                                                    apiClient.deleteUser(it, currentUser.token!!)
                                                    userList = apiClient.getUsers(currentUser.token!!)!!
                                                    scaffoldState.snackbarHostState.showSnackbar(
                                                        "Benutzer entfernt",
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
            Row(
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Text("")
            }
        }
    }
}