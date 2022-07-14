package com.example.whatsappclonejetpackfirebase.utils

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.whatsappclonejetpackfirebase.presentations.Splash
import com.example.whatsappclonejetpackfirebase.presentations.addprofile.AddProfile
import com.example.whatsappclonejetpackfirebase.presentations.contact.Contacts
import com.example.whatsappclonejetpackfirebase.presentations.main.Main
import com.example.whatsappclonejetpackfirebase.presentations.signUp.SignUp
import com.example.whatsappclonejetpackfirebase.presentations.utils.AppViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.runBlocking

@ExperimentalComposeUiApi
@ExperimentalPermissionsApi
@Composable
fun Navigations (
    activity: ComponentActivity
){
    val navController = rememberNavController()
    val appViewModel: AppViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = ScreenRoutes.SplashScreen.route){

        composable(
            route = ScreenRoutes.SplashScreen.route
        ){
            Splash(navController = navController)
        }

        composable(
            route = ScreenRoutes.SignUpScreen.route
        ){
            SignUp(navController = navController, activity = activity)
        }

        composable(
            route = ScreenRoutes.AddProfileScreen.route
        ){
            AddProfile(navHostController = navController)
        }

        composable(
            route = ScreenRoutes.MainScreen.route
        ){
            Main(navController = navController)
        }

        composable(
            route = ScreenRoutes.ContactsScreen.route
        ){
            Contacts(navController = navController)
        }

    }
}