package com.example.whatsappclonejetpackfirebase.presentations.addprofile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddProfileViewModel @Inject constructor(): ViewModel() {

    val username = mutableStateOf("")

    fun onChangeUsername(data: String){
        this.username.value = data
    }

}