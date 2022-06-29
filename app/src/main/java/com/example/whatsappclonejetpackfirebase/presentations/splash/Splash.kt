package com.example.whatsappclonejetpackfirebase.presentations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.whatsappclonejetpackfirebase.presentations.retry.Retry
import com.example.whatsappclonejetpackfirebase.presentations.splash.SplashViewModel
import com.example.whatsappclonejetpackfirebase.utils.ScreenRoutes

@Composable
fun Splash(
    navController: NavController,
){
    val splashViewModel: SplashViewModel = hiltViewModel()
    val screenRoutes = splashViewModel.screenRoutes.value
    val error = splashViewModel.error.value
    val errorMessage = splashViewModel.errorMessage.value
    val errorAction = splashViewModel::checkAuthAndDataUser
    val loading = splashViewModel.loading.value

    LaunchedEffect(key1 = screenRoutes){
        if (screenRoutes != null) {
            when (screenRoutes) {
                is ScreenRoutes.SignUpScreen -> navController.navigate(ScreenRoutes.SignUpScreen.route){
                    popUpTo(ScreenRoutes.SplashScreen.route){
                        inclusive = true
                    }
                }

                is ScreenRoutes.AddProfileScreen -> navController.navigate(ScreenRoutes.AddProfileScreen.route){
                    popUpTo(ScreenRoutes.SplashScreen.route){
                        inclusive = true
                    }
                }

                is ScreenRoutes.MainScreen -> navController.navigate(ScreenRoutes.MainScreen.route){
                    popUpTo(ScreenRoutes.SplashScreen.route){
                        inclusive = true
                    }
                }

                else -> {}
            }
        }
    }

    if (error) {
        Retry(errorAction = errorAction, errorMessage, loading = loading)
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "SPLASH SCREEN")
        }
    }

}