package com.example.whatsappclonejetpackfirebase.presentations.contact

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatsappclonejetpackfirebase.domain.model.ContactAccountModel
import com.example.whatsappclonejetpackfirebase.domain.model.ContactModel
import com.example.whatsappclonejetpackfirebase.domain.repository.ContactRepository
import com.example.whatsappclonejetpackfirebase.utils.ErrorMessageSubString
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    val db: FirebaseFirestore,
    val contactRepository: ContactRepository
) : ViewModel() {
    @SuppressLint("MutableCollectionMutableState")
    val contacts = mutableStateOf(mapOf<String, String>())
    val contactsAccount = mutableStateOf(listOf<ContactAccountModel>())
    val errorQueryContact = mutableStateOf(false)
    val errorQueryUserProfile = mutableStateOf(false)
    val errorMessage = mutableStateOf("")
    private var loopContacts = 0

    init {
        viewModelScope.launch {
            queryContacts()
        }
    }

    suspend fun queryContacts(){
        try {
            contacts.value = contactRepository.readContacts()
        } catch (e: Exception) {
            errorMessage.value = ErrorMessageSubString.calErrorMessage(e.message.toString())
            errorQueryContact.value = true
        }
    }

    suspend fun initQueryUserProfileFromContacts(){
        try {
            Log.d("CONTACTS", "start")
            var tempLoopContact = 0
            val contactsPhoneNumber = contacts.value.keys.toList()
            val contactsSize = contacts.value.size
            val tempContactAccounts = mutableListOf<ContactAccountModel>()
            val userProfileRef = db.collection("userProfile")
            Log.d("CONTACTS", "contacts ${contacts.value}")
            if (contactsSize > 0) {
                Log.d("CONTACTS", "size > 0")
                while(tempContactAccounts.size < 10 && tempLoopContact <= contactsSize) {
                    var subList = listOf<String>()
                    if((tempLoopContact + 10) <= contactsSize) {
                        subList = contactsPhoneNumber.subList(tempLoopContact, tempLoopContact + 10)
                        tempLoopContact = tempLoopContact + 10
                    } else {
                        subList = contactsPhoneNumber.subList(tempLoopContact, contactsSize)
                        tempLoopContact = tempLoopContact + 10
                    }
                    loopContacts = tempLoopContact
                    Log.d("CONTACTS", "sublist : ${subList}")
                    userProfileRef
                        .whereIn("phoneNumbers", subList)
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                Log.d("CONTACTS", "document ${document.data.toString()}")
                                val obj = ContactAccountModel(
                                    idUser = document.id,
                                    phoneNumber = document.get("phoneNumbers").toString(),
                                    photoUrl = document.get("photoUrl").toString(),
                                    about = document.get("about").toString()
                                )
                                tempContactAccounts.add(obj)
                            }
                            contactsAccount.value = tempContactAccounts
                        }
                        .addOnFailureListener { e ->
                            errorMessage.value = ErrorMessageSubString.calErrorMessage(e.message.toString())
                            errorQueryUserProfile.value = true
                        }
                }
            }
        } catch (e: Exception) {
            Log.d("CONTACTS", "Error : ${e.message.toString()}")
            errorMessage.value = ErrorMessageSubString.calErrorMessage(e.message.toString())
            errorQueryUserProfile.value = true
        }
    }

}