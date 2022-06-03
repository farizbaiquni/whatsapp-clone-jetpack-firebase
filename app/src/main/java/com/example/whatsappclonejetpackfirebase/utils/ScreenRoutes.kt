package com.example.whatsappclonejetpackfirebase.utils

sealed class ScreenRoutes(val route: String) {
    object MainScreen: ScreenRoutes("main_screen")
    object SignUpScreen: ScreenRoutes("signUp_screen")
    object ContactsScreen: ScreenRoutes("contacts_screen")
    object AddProfileScreen: ScreenRoutes("add_profile_screen")
    object SplashScreen: ScreenRoutes("splash_screen")
}
