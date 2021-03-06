package com.example.whatsappclonejetpackfirebase.presentations.signUp

sealed class SignUpState(value: String){
    object InputPhoneNumber: SignUpState(value = "input_phone_number")
    object InputOTPCode: SignUpState(value = "input_otp_code")
}