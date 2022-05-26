package com.example.whatsappclonejetpackfirebase.domain.repositories

import com.example.whatsappclonejetpackfirebase.domain.model.UserProfileModel

interface UserProfileRepository {
    fun getUserProfile(id: String): UserProfileModel?
}