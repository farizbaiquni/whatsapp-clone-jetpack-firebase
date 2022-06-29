package com.example.whatsappclonejetpackfirebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.ViewModelProvider
import com.example.whatsappclonejetpackfirebase.domain.repository.UserProfileRepository
import com.example.whatsappclonejetpackfirebase.presentations.signUp.SignUpViewModel
import com.example.whatsappclonejetpackfirebase.presentations.signUp.SignUpViewModelFactory
import com.example.whatsappclonejetpackfirebase.ui.theme.WhatsappCloneJetpackFirebaseTheme
import com.example.whatsappclonejetpackfirebase.utils.Navigations
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@ExperimentalComposeUiApi
@ExperimentalPermissionsApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        setContent {
            WhatsappCloneJetpackFirebaseTheme {
                Navigations(
                    this,
                )
            }
        }// End setContent
    }

}