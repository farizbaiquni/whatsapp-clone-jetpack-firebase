package com.example.whatsappclonejetpackfirebase.presentations.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel(){

    val tabIndex = mutableStateOf(0)

    fun onChangeTabIndex(index: Int){
        this.tabIndex.value = index
    }

}