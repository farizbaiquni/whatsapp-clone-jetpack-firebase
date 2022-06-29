package com.example.whatsappclonejetpackfirebase.presentations.main

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatsappclonejetpackfirebase.domain.model.UserProfileModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore
    ): ViewModel(){
    val tabIndex = mutableStateOf(0)

    //Loading status
    val initialLoading = mutableStateOf(false)
    val chatsLoading = mutableStateOf(false)
    val statusLoading = mutableStateOf(false)
    val callsLoading = mutableStateOf(false)

    val currentUser = mutableStateOf<FirebaseUser?>(null)
    var userProfileListener = mutableStateOf<ListenerRegistration?>(null)
    val userProfile: MutableState<UserProfileModel?> = mutableStateOf(null)

    //Error
    val isError = mutableStateOf<Boolean>(false)
    val errorMessage = mutableStateOf<String>("")

    init {
        viewModelScope.launch {
            fetchUser()
        }
    }


    suspend fun fetchUser(){
        initialLoading.value = true
        removeListenerUserProfile()
        val authUser = auth.currentUser
        if (authUser != null){
            currentUser.value = authUser
            listeningUserProfile((authUser))
            initialLoading.value = false
        } else {
            initialLoading.value = false
        }
    } // end userListener


    fun listeningUserProfile(authUser: FirebaseUser){
        var userProfileResult: UserProfileModel? = null
        val userRef = db.collection("users").document(authUser.uid)
        userProfileListener.value = userRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                errorMessage.value = error.message.toString()
                isError.value = true
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                userProfileResult = UserProfileModel(
                    idUser = snapshot.data?.getValue("idUser").toString(),
                    phoneNumbers = snapshot.data?.getValue("phoneNumbers").toString(),
                    photoUrl = snapshot.data?.getValue("photoUrl").toString(),
                    username = snapshot.data?.getValue("username").toString(),
                    about = snapshot.data?.getValue("about").toString(),
                )
            } else {
                // User profile not found
            }
            userProfile.value = userProfileResult
        }
    }


    fun removeListenerUserProfile () {
        userProfileListener.value?.remove()
    }


    fun onChangeTabIndex(index: Int){
        this.tabIndex.value = index
    }

}