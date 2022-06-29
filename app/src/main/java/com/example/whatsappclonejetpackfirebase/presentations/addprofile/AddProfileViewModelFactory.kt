package com.example.whatsappclonejetpackfirebase.presentations.addprofile

import androidx.compose.material.ScaffoldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.example.whatsappclonejetpackfirebase.utils.SnackbarJobController

class AddProfileViewModelFactory(
    private val scaffoldState: ScaffoldState,
    val snackbarJobController: SnackbarJobController,
    private val navHostController: NavHostController,
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddProfileViewModel::class.java)) {
            return AddProfileViewModel(
                scaffoldState = scaffoldState,
                snackbarJobController = snackbarJobController,
                navHostController = navHostController,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}