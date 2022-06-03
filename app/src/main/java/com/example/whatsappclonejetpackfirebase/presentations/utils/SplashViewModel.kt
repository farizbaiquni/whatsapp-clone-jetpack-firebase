package com.example.whatsappclonejetpackfirebase.presentations.utils

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatsappclonejetpackfirebase.domain.model.UserProfileModel
import com.example.whatsappclonejetpackfirebase.domain.repository.UserProfileRepository
import com.example.whatsappclonejetpackfirebase.utils.ScreenRoutes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val userProfileRepository: UserProfileRepository
): ViewModel() {
    val authUser = mutableStateOf<FirebaseUser?>(null)
    val userProfile = mutableStateOf<UserProfileModel?>(null)
    val screenRoutes = mutableStateOf<ScreenRoutes?>(null)

    init {
        viewModelScope.launch {
            val authResult = checkAuthenticationUser()
            if (authResult != null) {
                val userResult = userProfileRepository.getUserProfile(authResult.uid)
                if (userResult != null) {
                    if (userResult.username != null && userResult.about != null) {
                        screenRoutes.value = ScreenRoutes.MainScreen
                    } else {
                        screenRoutes.value = ScreenRoutes.AddProfileScreen
                    }
                } else {
                    screenRoutes.value = ScreenRoutes.AddProfileScreen
                }
            } else {
                screenRoutes.value = ScreenRoutes.SignUpScreen
            }
        }
    }

    suspend fun checkAuthenticationUser(): FirebaseUser?{
        var currentUser: FirebaseUser? = auth.currentUser
        return currentUser
    }
}