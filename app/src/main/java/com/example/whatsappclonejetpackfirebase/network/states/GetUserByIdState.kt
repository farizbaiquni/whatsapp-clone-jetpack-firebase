package com.example.whatsappclonejetpackfirebase.network.states

import com.example.whatsappclonejetpackfirebase.domain.model.UserProfileModel

interface GetUserByIdState {
    fun onSuccess(data: Triple<Boolean, String, UserProfileModel?>)
    fun onNoData(data: Triple<Boolean, String, UserProfileModel?>)
    fun onError(databaseError: Triple<Boolean, String, UserProfileModel?>)
}