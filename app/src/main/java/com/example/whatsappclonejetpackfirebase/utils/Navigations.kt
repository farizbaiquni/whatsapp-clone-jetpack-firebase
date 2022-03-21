package com.example.whatsappclonejetpackfirebase.utils

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.whatsappclonejetpackfirebase.presentations.signUp.SignUp

@Composable
fun Navigations(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ScreenRoutes.SignUpScreen.route){
        composable(
            route = ScreenRoutes.SignUpScreen.route
        ){
            SignUp()
        }
    }
}