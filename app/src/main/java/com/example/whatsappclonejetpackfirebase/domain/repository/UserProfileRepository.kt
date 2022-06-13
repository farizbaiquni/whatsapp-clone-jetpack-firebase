package com.example.whatsappclonejetpackfirebase.domain.repository

import com.example.whatsappclonejetpackfirebase.network.states.GetUserByIdState

interface UserProfileRepository {
    suspend fun getUserById(id: String, listener: GetUserByIdState)
}