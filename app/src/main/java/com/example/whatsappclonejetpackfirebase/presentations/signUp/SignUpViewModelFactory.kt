package com.example.whatsappclonejetpackfirebase.presentations.signUp

import android.app.Activity
import androidx.compose.material.ScaffoldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore

class SignUpViewModelFactory(private val activity: Activity, private val scaffoldState: ScaffoldState): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(activity, scaffoldState) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}