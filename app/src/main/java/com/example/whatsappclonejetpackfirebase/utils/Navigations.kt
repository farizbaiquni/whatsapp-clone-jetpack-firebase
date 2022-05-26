package com.example.whatsappclonejetpackfirebase.utils

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.whatsappclonejetpackfirebase.domain.model.UserProfileModel
import com.example.whatsappclonejetpackfirebase.domain.repositories.UserProfileRepository
import com.example.whatsappclonejetpackfirebase.presentations.addprofile.AddProfile
import com.example.whatsappclonejetpackfirebase.presentations.contact.Contacts
import com.example.whatsappclonejetpackfirebase.presentations.main.Main
import com.example.whatsappclonejetpackfirebase.presentations.signUp.SignUp
import com.example.whatsappclonejetpackfirebase.presentations.signUp.SignUpViewModel
import com.example.whatsappclonejetpackfirebase.presentations.utils.AppViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

@ExperimentalPermissionsApi
@Composable
fun Navigations(
    signUpViewModel: SignUpViewModel,
    currentUser: FirebaseUser?,
    userProfileRepository: UserProfileRepository,
){
    val navController = rememberNavController()
    val appViewModel: AppViewModel = hiltViewModel()
    var initialRoute: String = ScreenRoutes.SignUpScreen.route

    if (currentUser !== null){
        val userProfile: UserProfileModel? = userProfileRepository.getUserProfile(currentUser.uid)
        if (userProfile !== null){
            if (userProfile.username !== null){
                initialRoute = ScreenRoutes.AddProfile.route
            } else {
                initialRoute = ScreenRoutes.MainScreen.route
            }
        } else {
            initialRoute = ScreenRoutes.AddProfile.route
        }
    }


    NavHost(navController = navController, startDestination = initialRoute){
        composable(
            route = ScreenRoutes.SignUpScreen.route
        ){
            SignUp(signUpViewModel, navController)
        }
        composable(
            route = ScreenRoutes.MainScreen.route
        ){
            Main(navController, appViewModel)
        }
        composable(
            route = ScreenRoutes.ContactsScreen.route
        ){
            Contacts(navController, appViewModel)
        }
        composable(
            route = ScreenRoutes.AddProfile.route
        ){
            AddProfile(navController = navController)
        }
    }
}