package com.example.whatsappclonejetpackfirebase.presentations.signUp

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SignUp(
    signUpViewModel: SignUpViewModel,
){
    val prefixPhoneNumbers = signUpViewModel.prefixPhoneNumbers.value
    val postfixPhoneNumbers = signUpViewModel.postfixPhoneNumbers.value
    val state = signUpViewModel.state.value
    val otpCode = signUpViewModel.otpCode.value
    val otpCodeTimerLeft = signUpViewModel.otpCodeTimerLeft.value
    val verifyingProgress = signUpViewModel.verifyingProgress.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp, horizontal = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

        when(state){
            is SignUpState.InputPhoneNumber -> {
                SignUpInputPhoneNumbers(
                    postfixPhoneNumbers = postfixPhoneNumbers,
                    prefixPhoneNumbers = prefixPhoneNumbers,
                    onChangePostfixPhoneNumbers = signUpViewModel::onChangePostfixPhoneNumbers,
                    startSignUpWithPhoneNumbers = signUpViewModel::startSignUpWithPhoneNumbers,
                )
            }

            is SignUpState.InputOTPCode -> {
                SignUpOTP(
                    prefixPhoneNumbers = prefixPhoneNumbers,
                    postfixPhoneNumbers = postfixPhoneNumbers,
                    otpCode = otpCode,
                    onChangeOTPCode = signUpViewModel::onChangeOTPCode,
                    otpCodeTimerLeft = otpCodeTimerLeft,
                    resendVerificationCode = signUpViewModel::resendVerificationCode,
                    verifyingProgress = verifyingProgress
                )
            }
        }

    }
}

//@Preview(showBackground = true)
//@Composable
//fun SignUpPreview() {
//    WhatsappCloneJetpackFirebaseTheme {
//        SignUp()
//    }
//}