package com.example.whatsappclonejetpackfirebase.network.repository

import com.example.whatsappclonejetpackfirebase.domain.model.UserProfileModel
import com.example.whatsappclonejetpackfirebase.domain.repository.UserProfileRepository
import com.google.firebase.firestore.FirebaseFirestore

class UserProfileRepositoryImpl(
    private val db: FirebaseFirestore,
): UserProfileRepository {
    override suspend fun getUserProfile(id: String): UserProfileModel? {
        var user: UserProfileModel? = null
        val docRef = db.collection("userProfile").document(id)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    user = UserProfileModel(
                        idUser = document.get("idUser").toString(),
                        phoneNumbers = document.get("phoneNumbers").toString(),
                        photoUrl = if (document.get("photoUrl") !== null) document.get("photoUrl").toString() else null,
                        username = if (document.get("username") !== null) document.get("username").toString() else null,
                        about = if (document.get("about") !== null) document.get("about").toString() else null,
                    )
                } else {
                    throw Exception("user profile not found")
                }
            }
            .addOnFailureListener { exception ->
                throw Exception(exception)
            }
        return user
    }
}