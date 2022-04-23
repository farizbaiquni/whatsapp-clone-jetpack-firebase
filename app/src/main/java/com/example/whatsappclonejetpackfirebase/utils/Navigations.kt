package com.example.whatsappclonejetpackfirebase.utils

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.whatsappclonejetpackfirebase.presentations.addprofile.AddProfile
import com.example.whatsappclonejetpackfirebase.presentations.contact.Contacts
import com.example.whatsappclonejetpackfirebase.presentations.main.Main
import com.example.whatsappclonejetpackfirebase.presentations.signUp.SignUp
import com.example.whatsappclonejetpackfirebase.presentations.signUp.SignUpViewModel
import com.example.whatsappclonejetpackfirebase.presentations.utils.AppViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@ExperimentalPermissionsApi
@Composable
fun Navigations(signUpViewModel: SignUpViewModel){
    val navController = rememberNavController()
    val appViewModel: AppViewModel = hiltViewModel()
    NavHost(navController = navController, startDestination = ScreenRoutes.AddProfile.route){
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