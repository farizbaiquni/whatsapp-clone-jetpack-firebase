package com.example.whatsappclonejetpackfirebase.domain.model

import java.util.*

data class RoomModel(
    val idRoom: String,
    val type: String,
    val participants: List<String>,
    val photoGroup: String,
    val createdAt: Date?,
    val lastChatIdUser: String,
    val lastChatPhoneNumber: String,
    val lastChatAttachContentType: String,
    val lastChatMessage: String,
    val lastChatCreatedAt: Date,
)
