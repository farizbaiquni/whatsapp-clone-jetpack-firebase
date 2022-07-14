package com.example.whatsappclonejetpackfirebase.presentations.main

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatsappclonejetpackfirebase.domain.model.RoomModel
import com.example.whatsappclonejetpackfirebase.domain.model.UserProfileModel
import com.example.whatsappclonejetpackfirebase.utils.enums.RoomTypeEnum
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
    var chatsListener = mutableStateOf<ListenerRegistration?>(null)
    val userProfile: MutableState<UserProfileModel?> = mutableStateOf(null)
    val rooms: MutableState<List<RoomModel>> = mutableStateOf(listOf())
    val idUsersPersonalRoom: MutableState<Set<String>> = mutableStateOf(setOf())

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


    @Suppress("UNCHECKED_CAST")
    fun listeningChats(idAuthUser: String){
        removeListenerChats()
        resetErrorAndLoadingChat()
        db.collection("rooms")
            .whereArrayContains("participants", idAuthUser)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    isError.value = true
                    errorMessage.value = e.message.toString()
                    return@addSnapshotListener
                }
                val tempRooms = mutableListOf<RoomModel>()
                //untuk mengambil photo profile dan username dari room persobal
                val tempIdUsersPersonalRoom: MutableSet<String> = mutableSetOf()
                tempIdUsersPersonalRoom.addAll(idUsersPersonalRoom.value)
                for (doc in value!!) {
                    val roomObj = RoomModel(
                        idRoom = doc.get("idRoom").toString(),
                        type = doc.get("type").toString(),
                        participants = (doc.get("participants") as List<String>),
                        photoGroup = doc.get("photoGroup").toString(),
                        createdAt = doc.getTimestamp("createdAt")!!.toDate(),
                        lastChatIdUser = doc.get("lastChatIdUser").toString(),
                        lastChatPhoneNumber = doc.get("lastChatPhoneNumber").toString(),
                        lastChatAttachContentType = doc.get("lastChatAttachContentType").toString(),
                        lastChatMessage = doc.get("lastChatMessage").toString(),
                        lastChatCreatedAt = doc.getTimestamp("lastChatCreatedAt")!!.toDate(),
                    )
                    if(doc.get("type").toString() === RoomTypeEnum.PERSONALTYPE.type) {
                        val participants = (doc.get("participants") as List<String>)
                        if(participants.indexOf(idAuthUser) == 0) {
                            tempIdUsersPersonalRoom.add(participants.get(1))
                        } else {
                            tempIdUsersPersonalRoom.add(participants.get(0))
                        }
                    }
                    tempRooms.add(roomObj)
                }
                resetErrorAndLoadingChat()
                rooms.value = tempRooms
            }
    }


    fun listeningUserProfile(authUser: FirebaseUser){
        var userProfileResult: UserProfileModel? = null
        val userRef = db.collection("userProfile").document(authUser.uid)
        userProfileListener.value = userRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                errorMessage.value = error.message.toString()
                isError.value = true
                return@addSnapshotListener
            }
            if(snapshot != null && snapshot.exists()) {
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

    fun removeListenerChats () {
        chatsListener.value?.remove()
    }

    fun onChangeTabIndex(index: Int){
        this.tabIndex.value = index
    }

    fun resetErrorAndLoadingChat() {
        isError.value = false
        errorMessage.value = ""
        chatsLoading.value = true
    }

}