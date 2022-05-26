package com.example.whatsappclonejetpackfirebase.presentations.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatsappclonejetpackfirebase.domain.ContactRepository
import com.example.whatsappclonejetpackfirebase.domain.model.ContactModel
import com.example.whatsappclonejetpackfirebase.domain.model.UserProfileModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val contactRepository: ContactRepository
): ViewModel(){

    val db = Firebase.firestore
    private var userRef: DocumentReference? = null

    val userProfile: MutableState<UserProfileModel?> = mutableStateOf(null)
    val contacts: MutableState<ArrayList<ContactModel>> = mutableStateOf(arrayListOf())

    fun queryContacts(){
        viewModelScope.launch {
            contacts.value.addAll(contactRepository.readContacts())
        }
    }// queryContacts

    fun userListener(idUser: String){
        viewModelScope.launch {
            userRef = db.collection("users").document(idUser)
            userRef?.let {
                it.addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        userProfile.value = UserProfileModel(
                            idUser = snapshot.data?.getValue("idUser").toString(),
                            phoneNumbers = snapshot.data?.getValue("phoneNumbers").toString(),
                            photoUrl = snapshot.data?.getValue("photoUrl").toString(),
                            username = snapshot.data?.getValue("username").toString(),
                            about = snapshot.data?.getValue("about").toString(),
                        )
                    } else {
                        userProfile.value = null
                    }
                }
            }
        }
    }// end userListener

}