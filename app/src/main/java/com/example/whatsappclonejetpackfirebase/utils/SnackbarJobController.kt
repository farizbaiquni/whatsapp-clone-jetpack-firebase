package com.example.whatsappclonejetpackfirebase.utils

import androidx.compose.material.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SnackbarJobController(
    private val scope: CoroutineScope
) {
    private var snackbarJob: Job? = null

    fun getScope() = scope
    fun showSnackbar(
        snackbarHostState: SnackbarHostState,
        message: String,
        actionLabel: String,
    ) {
        if(snackbarJob == null) {
            snackbarJob = scope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel,
                )
                cancelSnackbarJob()
            }
        } else {
            cancelSnackbarJob()
            snackbarJob = scope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel,
                )
            }
        }
    }

    private fun cancelSnackbarJob(){
        this.snackbarJob?.let {
            it.cancel()
            snackbarJob = Job()
        }
    }
}