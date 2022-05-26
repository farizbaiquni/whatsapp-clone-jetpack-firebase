package com.example.whatsappclonejetpackfirebase.domain.model

data class UserProfileModel(
    val idUser: String,
    val phoneNumbers: String,
    val photoUrl: String?,
    val username: String?,
    val about: String?,
)
