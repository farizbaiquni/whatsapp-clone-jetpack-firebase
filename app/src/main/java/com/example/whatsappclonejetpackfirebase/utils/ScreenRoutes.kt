package com.example.whatsappclonejetpackfirebase.utils

sealed class ScreenRoutes(val route: String) {
    object MainScreen: ScreenRoutes("main_screen")
    object SignUpScreen: ScreenRoutes("signUp_screen")
    object ContactsScreen: ScreenRoutes("contacts_screen")
    object AddProfile: ScreenRoutes("add_profile_screen")
}
