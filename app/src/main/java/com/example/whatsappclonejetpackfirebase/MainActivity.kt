package com.example.whatsappclonejetpackfirebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.whatsappclonejetpackfirebase.presentations.signUp.SignUpViewModel
import com.example.whatsappclonejetpackfirebase.presentations.signUp.SignUpViewModelFactory
import com.example.whatsappclonejetpackfirebase.ui.theme.WhatsappCloneJetpackFirebaseTheme
import com.example.whatsappclonejetpackfirebase.utils.Navigations
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint


@ExperimentalPermissionsApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var signUpViewModelFactory: SignUpViewModelFactory
    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        signUpViewModelFactory = SignUpViewModelFactory(this)
        signUpViewModel = ViewModelProvider(this, signUpViewModelFactory).get(SignUpViewModel::class.java)

        super.onCreate(savedInstanceState)
        setContent {
            WhatsappCloneJetpackFirebaseTheme {
                Navigations(
                    signUpViewModel = signUpViewModel
                )
            }
        }// End setContent
    }

}