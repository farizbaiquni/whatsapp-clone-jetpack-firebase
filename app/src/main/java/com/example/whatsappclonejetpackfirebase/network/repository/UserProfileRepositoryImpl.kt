package com.example.whatsappclonejetpackfirebase.network.repository

import android.util.Log
import com.example.whatsappclonejetpackfirebase.domain.model.UserProfileModel
import com.example.whatsappclonejetpackfirebase.domain.repository.UserProfileRepository
import com.example.whatsappclonejetpackfirebase.network.states.GetUserByIdState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source

class UserProfileRepositoryImpl(
    private val db: FirebaseFirestore,
): UserProfileRepository {
    override suspend fun getUserById(id: String, listener: GetUserByIdState) {
        val docRef = db.collection("userProfile").document(id)
        docRef.get(Source.SERVER).addOnSuccessListener { document ->
            if (document != null) {
                if(document.exists()) {
                    if(document.get("username") != null && document.get("about") != null && document.get("phoneNumbers") != null) {
                        Log.d("USER PROFILE", "${document.data}")
                        val user = UserProfileModel(
                            idUser = document.get("idUser").toString(),
                            photoUrl = document.get("photoUrl")?.toString() ?: "",
                            phoneNumbers = if (document.get("phoneNumbers") !== null) document.get("phoneNumbers").toString() else null,
                            username = if (document.get("username") !== null) document.get("username").toString() else null,
                            about = if (document.get("about") !== null) document.get("about").toString() else null,
                        )
                        listener.onSuccess(Triple(false, "", user))
                    } else {
                        listener.onNoData(objectTripleNoData())
                    }
                } else {
                    listener.onNoData(objectTripleNoData())
                }
            } else {
                listener.onNoData(objectTripleNoData())
            }
        }.addOnFailureListener { exception ->
            Log.d("USER PROFILE", "ERROR")
            listener.onError((Triple(true, exception.message.toString(), null)))
        }
    }

    private fun objectTripleNoData(): Triple<Boolean, String, UserProfileModel?> {
        return Triple(false, "", null)
    }
}




