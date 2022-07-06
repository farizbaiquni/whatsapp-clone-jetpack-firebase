package com.example.whatsappclonejetpackfirebase.presentations.components

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun SnackbarComponent(
    snackbarHostState: SnackbarHostState,
) {
    SnackbarHost(hostState = snackbarHostState) {
        Snackbar(
            action = {
                 TextButton(
                     onClick = {
                         snackbarHostState.currentSnackbarData?.dismiss()
                     }
                 ) {
                     Text(
                         text = "Dismiss",
                         color = MaterialTheme.colors.onError
                     )
                 }
            },
            actionOnNewLine = false,
            shape = MaterialTheme.shapes.small,
            backgroundColor = MaterialTheme.colors.error,
            contentColor = MaterialTheme.colors.error,
            elevation = 6.dp,
        ) {
            Text(
                text = snackbarHostState.currentSnackbarData?.message?:"Dismiss",
                color = MaterialTheme.colors.onError
            )

        }
    }
}