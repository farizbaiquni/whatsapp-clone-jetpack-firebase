package com.example.whatsappclonejetpackfirebase.presentations.splash

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatsappclonejetpackfirebase.domain.model.UserProfileModel
import com.example.whatsappclonejetpackfirebase.domain.repository.UserProfileRepository
import com.example.whatsappclonejetpackfirebase.utils.ScreenRoutes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import com.example.whatsappclonejetpackfirebase.network.states.GetUserByIdState

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val userProfileRepository: UserProfileRepository
): ViewModel() {
    val screenRoutes = mutableStateOf<ScreenRoutes?>(null)
    val error = mutableStateOf<Boolean>(false)
    val errorMessage = mutableStateOf<String>("")
    val loading = mutableStateOf<Boolean>(false)

    init {
        checkAuthAndDataUser()
    }

    suspend fun checkAuthenticationUser(): FirebaseUser?{
        var currentUser: FirebaseUser? = auth.currentUser
        return currentUser
    }

    suspend fun checkDataUser(currentUser: FirebaseUser) {
        try {
            userProfileRepository.getUserById(currentUser.uid, object: GetUserByIdState {
                override fun onSuccess(data: Triple<Boolean, String, UserProfileModel?>) {
                    screenRoutes.value = ScreenRoutes.MainScreen
                    loading.value = false
                }

                override fun onNoData(data: Triple<Boolean, String, UserProfileModel?>) {
                    screenRoutes.value = ScreenRoutes.AddProfileScreen
                    loading.value = false
                }

                override fun onError(databaseError: Triple<Boolean, String, UserProfileModel?>) {
                    errorMessage.value = databaseError.second
                    error.value = databaseError.first
                    loading.value = false
                }
            })

        } catch (e: Exception){ }
    }

    fun checkAuthAndDataUser(){
        viewModelScope.launch {
            loading.value = true
            val currentUser = checkAuthenticationUser()
            if (currentUser != null) {
                checkDataUser(currentUser)
            } else {
                screenRoutes.value = ScreenRoutes.SignUpScreen
                loading.value = false
            }
        }
    }

}