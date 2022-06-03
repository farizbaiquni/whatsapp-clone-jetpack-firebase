package com.example.whatsappclonejetpackfirebase.domain.repository

import com.example.whatsappclonejetpackfirebase.domain.model.UserProfileModel

interface UserProfileRepository {
    suspend fun getUserProfile(id: String): UserProfileModel?
}