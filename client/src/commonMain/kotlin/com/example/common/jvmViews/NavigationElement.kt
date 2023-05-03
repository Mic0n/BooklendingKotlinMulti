package com.example.common.jvmViews

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun addNavigationElement(
    state: String, text: String, icon: ImageVector, scope: CoroutineScope, scaffoldState: ScaffoldState, updateState: (String) -> Unit
) {
    Row {
        TextButton(
            onClick = {
                updateState(state)
                scope.launch {
                    scaffoldState.drawerState.apply {
                        close()
                    }
                }
            },
            modifier = Modifier.padding(start = 16.dp, end = 16.dp).fillMaxWidth(),
        ) {
            Icon(icon, contentDescription = "text")
            Text(text, modifier = Modifier.padding(start = 16.dp).fillMaxWidth())

        }
    }
}