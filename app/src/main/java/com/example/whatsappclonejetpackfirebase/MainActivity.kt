package com.example.whatsappclonejetpackfirebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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


@ExperimentalPermissionsApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var signUpViewModelFactory: SignUpViewModelFactory
    private lateinit var signUpViewModel: SignUpViewModel
    @Inject lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpViewModelFactory = SignUpViewModelFactory(this, db)
        signUpViewModel = ViewModelProvider(this, signUpViewModelFactory).get(SignUpViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        setContent {
            WhatsappCloneJetpackFirebaseTheme {
                Navigations(
                    signUpViewModel = signUpViewModel,
                )
            }
        }// End setContent
    }

}