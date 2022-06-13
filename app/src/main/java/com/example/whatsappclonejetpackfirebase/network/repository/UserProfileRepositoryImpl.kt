package com.example.whatsappclonejetpackfirebase.network.repository

import com.example.whatsappclonejetpackfirebase.domain.model.UserProfileModel
import com.example.whatsappclonejetpackfirebase.domain.repository.UserProfileRepository
import com.example.whatsappclonejetpackfirebase.network.states.GetUserByIdState
import com.google.firebase.firestore.FirebaseFirestore

class UserProfileRepositoryImpl(
    private val db: FirebaseFirestore,
): UserProfileRepository {
    override suspend fun getUserById(id: String, listener: GetUserByIdState) {
        val docRef = db.collection("userProfile").document(id)
        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
                val user = UserProfileModel(
                    idUser = document.get("idUser").toString(),
                    phoneNumbers = document.get("phoneNumbers").toString(),
                    photoUrl = if (document.get("photoUrl") !== null) document.get("photoUrl").toString() else null,
                    username = if (document.get("username") !== null) document.get("username").toString() else null,
                    about = if (document.get("about") !== null) document.get("about").toString() else null,
                )
                listener.onSuccess(Triple(false, "", user))
            } else {
                listener.onNoData(Triple(false, "", null))
            }
        }.addOnFailureListener { exception ->
            listener.onError((Triple(true, exception.message.toString(), null)))
        }
    }
}




