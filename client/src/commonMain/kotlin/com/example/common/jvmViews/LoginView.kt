package com.example.common.jvmViews

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import models.User
import models.UserRequest

@Composable
fun renderLogin(
    apiClient: ApiClient,
    scope: CoroutineScope,
    updateState: (String) -> Unit,
    updateCurrentUser: (User) -> Unit,
    currentUser: User
) {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scaffoldState = rememberScaffoldState()
    Scaffold(scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Anmelden",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    label = { Text("Benutzername") },
                    leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Person") },
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.widthIn(0.dp, 400.dp).fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    label = { Text("Passwort") },
                    value = password,
                    onValueChange = { password = it },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Lock") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                                    updateState("loginSelection")
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
                                        updateCurrentUser(apiClient.loginUser(UserRequest(name, password)))
                                        if (currentUser.userId == "0") {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                "Anmeldung Fehlgeschlagen", actionLabel = "Ok"
                                            )
                                        }
                                    }
                                },
                                modifier = Modifier.widthIn(0.dp, 400.dp).fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp),
                                enabled = name != "" && password != ""
                            ) {
                                Text(text = "Anmelden")
                            }
                        }
                    }

                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                updateState("loginSelection")
                            },
                            Modifier.widthIn(0.dp, 200.dp).fillMaxWidth()
                                .padding(start = 16.dp, end = 4.dp, top = 4.dp, bottom = 4.dp)
                        ) {
                            Text("Zurück")
                        }
                        Button(
                            onClick = {
                                scope.launch {
                                    updateCurrentUser(apiClient.loginUser(UserRequest(name, password)))
                                    if (currentUser.userId == "0") {
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            "Anmeldung Fehlgeschlagen", actionLabel = "Ok"
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.widthIn(0.dp, 200.dp).fillMaxWidth()
                                .padding(start = 4.dp, end = 16.dp, top = 4.dp, bottom = 4.dp),
                            enabled = name != "" && password != ""
                        ) {
                            Text(text = "Anmelden")
                        }
                    }
                }
            }
        }
    }
    if (currentUser.userId != "0") {
        updateState("main")
    }
}