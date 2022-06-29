package com.example.whatsappclonejetpackfirebase.utils

object ErrorMessageSubString {
    fun calErrorMessage(message: String): String{
        if(message.length > 30) {
            return message.substring(0, 30) + "..."
        } else {
            return message
        }
    }
}