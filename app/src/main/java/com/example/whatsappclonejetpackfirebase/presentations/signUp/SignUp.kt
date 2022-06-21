package com.example.whatsappclonejetpackfirebase.presentations.signUp

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.whatsappclonejetpackfirebase.utils.ScreenRoutes

@Composable
fun SignUp(
    navController: NavHostController,
    activity: ComponentActivity
){
    val scaffoldState = rememberScaffoldState()
    val signUpViewModel: SignUpViewModel = viewModel(
        factory = SignUpViewModelFactory(activity, scaffoldState)
    )

    val prefixPhoneNumbers = signUpViewModel.prefixPhoneNumbers.value
    val postfixPhoneNumbers = signUpViewModel.postfixPhoneNumbers.value
    val screenState = signUpViewModel.screenState.value
    val otpCode = signUpViewModel.otpCode.value
    val otpCodeTimerLeft = signUpViewModel.otpCodeTimerLeft.value
    val verifyingProgress = signUpViewModel.verifyingProgress.value
    val snackbarHostState = signUpViewModel.snackbarHostState.value
    val snackbarJobController = signUpViewModel.snackbarJobController

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            scaffoldState.snackbarHostState
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 30.dp, horizontal = 7.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {

            LaunchedEffect(key1 = screenState){
                when(screenState) {
                    is SignUpState.ToAddProfileScreen -> {
                        navController.navigate(ScreenRoutes.AddProfileScreen.route){
                            popUpTo(ScreenRoutes.SignUpScreen.route){
                                inclusive = true
                            }
                        }
                    }

                    is SignUpState.ToMainScreen -> {
                        navController.navigate(ScreenRoutes.MainScreen.route){
                            popUpTo(ScreenRoutes.SignUpScreen.route){
                                inclusive = true
                            }
                        }
                    }
                    else -> {}
                }
            }

            when(screenState) {
                is SignUpState.InputPhoneNumber -> {
                    SignUpInputPhoneNumbers(
                        postfixPhoneNumbers = postfixPhoneNumbers,
                        prefixPhoneNumbers = prefixPhoneNumbers,
                        onChangePostfixPhoneNumbers = signUpViewModel::onChangePostfixPhoneNumbers,
                        startSignUpWithPhoneNumbers = signUpViewModel::startSignUpWithPhoneNumbers,
                        snackbarHostState = snackbarHostState,
                    )
                }

                else -> {
                    SignUpOTP(
                        prefixPhoneNumbers = prefixPhoneNumbers,
                        postfixPhoneNumbers = postfixPhoneNumbers,
                        otpCode = otpCode,
                        onChangeOTPCode = signUpViewModel::onChangeOTPCode,
                        otpCodeTimerLeft = otpCodeTimerLeft,
                        resendVerificationCode = signUpViewModel::resendVerificationCode,
                        verifyingProgress = verifyingProgress,
                        verifyOTPCode = signUpViewModel::verifyOTPCode,
                        snackbarHostState = snackbarHostState,
                        snackbarJobController = snackbarJobController,
                    )
                }
            }
        } // end column
    }
} // end fun compose



//@Preview(showBackground = true)
//@Composable
//fun SignUpPreview() {
//    WhatsappCloneJetpackFirebaseTheme {
//        SignUp()
//    }
//}