package com.example.whatsappclonejetpackfirebase.presentations.signUp

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.whatsappclonejetpackfirebase.utils.ScreenRoutes

@Composable
fun SignUp(
    signUpViewModel: SignUpViewModel,
    navController: NavHostController,
){
    val prefixPhoneNumbers = signUpViewModel.prefixPhoneNumbers.value
    val postfixPhoneNumbers = signUpViewModel.postfixPhoneNumbers.value
    val screenState = signUpViewModel.screenState.value
    val otpCode = signUpViewModel.otpCode.value
    val otpCodeTimerLeft = signUpViewModel.otpCodeTimerLeft.value
    val verifyingProgress = signUpViewModel.verifyingProgress.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp, horizontal = 7.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

        when(screenState){
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
                    verifyingProgress = verifyingProgress,
                    verifyOTPCode = signUpViewModel::verifyOTPCode,
                )
            }

            is SignUpState.ToAddProfileScreen -> {
                navController.navigate(ScreenRoutes.AddProfile.route){
                    popUpTo(ScreenRoutes.AddProfile.route){
                        inclusive = true
                    }
                }
            }

            is SignUpState.ToMainScreen -> {
                navController.navigate(ScreenRoutes.MainScreen.route){
                    popUpTo(ScreenRoutes.MainScreen.route){
                        inclusive = true
                    }
                }
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