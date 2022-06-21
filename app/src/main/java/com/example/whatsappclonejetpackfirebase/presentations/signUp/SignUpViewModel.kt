package com.example.whatsappclonejetpackfirebase.presentations.signUp

import android.app.Activity
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatsappclonejetpackfirebase.utils.SnackbarJobController
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.lang.NullPointerException
import java.util.concurrent.TimeUnit


class SignUpViewModel(activity: Activity, scaffoldState: ScaffoldState): ViewModel() {
    val db = FirebaseFirestore.getInstance()
    val prefixPhoneNumbers = mutableStateOf("+62")
    val postfixPhoneNumbers = mutableStateOf("")
    val otpCodeTimerLeft = mutableStateOf<Long>(65)
    val otpCode = mutableStateOf("")
    val myActivity: MutableState<Activity> = mutableStateOf(activity)
    val myForceResendingToken: MutableState<PhoneAuthProvider.ForceResendingToken?> = mutableStateOf(null)
    val myVerificationId: MutableState<String?> = mutableStateOf(null)
    val screenState: MutableState<SignUpState> = mutableStateOf(SignUpState.InputPhoneNumber)
    val verifyingProgress = mutableStateOf(false)
    val snackbarHostState = mutableStateOf(scaffoldState.snackbarHostState)
    val snackbarJobController = SnackbarJobController(viewModelScope)

    fun startSignUpWithPhoneNumbers(){
        val fullPhoneNumbers = "+62" + postfixPhoneNumbers.value
        viewModelScope.launch {
            try {
                Log.d("PHONE NUMBERS", fullPhoneNumbers)
                var auth: FirebaseAuth = Firebase.auth

                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(fullPhoneNumbers)
                    .setTimeout(65L, TimeUnit.SECONDS)
                    .setActivity(myActivity.value)
                    .setCallbacks(callbacks)
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            } catch (e: Exception){
                Log.d("FAILED START", e.toString())
            }
        }
    } // End signUpWithPhoneNumber


    fun verifyOTPCode(){
        try {
            verifyingProgress.value = true
            val credential = PhoneAuthProvider.getCredential(myVerificationId.value!!, otpCode.value)
            signInWithPhoneNumber(credential)
        } catch (e: Exception){
            verifyingProgress.value = false
            screenState.value = SignUpState.InputPhoneNumber
        }
    }


    fun startTimerOTPCode(){
        viewModelScope.launch {
            object : CountDownTimer(65000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    otpCodeTimerLeft.value = millisUntilFinished / 1000
                }
                override fun onFinish() { }
            }.start()
        }
    }


    fun signInWithPhoneNumber(credential: PhoneAuthCredential){
        var auth: FirebaseAuth = Firebase.auth

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this.myActivity.value) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's =
                    Log.d("LOGIN ", "SUCCESS")
                    val user = task.result?.user
                    if(user !== null){
                        checkDataUserProfile(user.uid)
                    } else {
                        Log.d("USER NULL", "inside sign in")
                        this.verifyingProgress.value = false
                        this.screenState.value = SignUpState.InputPhoneNumber
                    }
                }
        }.addOnFailureListener { e ->
                run {
                    verifyingProgress.value = false
                    snackbarJobController.showSnackbar(
                        snackbarHostState = snackbarHostState.value,
                        message = e.message.toString(),
                        actionLabel = "Dismiss"
                    )
                }
            }
    } // End signInWithPhoneNumber


    fun checkDataUserProfile(id: String){
        val docRef = db.collection("userProfile").document(id)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("DATA USER FOUND", "inside checkDataUserProfile")
                    if(document.get("username") !== null){
                        Log.d("DATA USERNAME FOUND", "inside checkDataUserProfile")
                        this.verifyingProgress.value = false
                        this.screenState.value = SignUpState.ToMainScreen
                    } else {
                        Log.d("DATA USERNAME NOT FOUND", "inside checkDataUserProfile")
                        this.verifyingProgress.value = false
                        this.screenState.value = SignUpState.ToAddProfileScreen
                    }
                } else {
                    Log.d("DATA USER PROFILE NULL", "inside checkDataUserProfile")
                    this.verifyingProgress.value = false
                    this.screenState.value = SignUpState.InputPhoneNumber
                }
            }
            .addOnFailureListener { exception ->
                Log.d("FAILED CHECK DATA USER", "exception: ", exception)
                this.verifyingProgress.value = false
                this.screenState.value = SignUpState.InputPhoneNumber
            }
    }


    fun resendVerificationCode() {
        if(myForceResendingToken.value != null){
            viewModelScope.launch {
                try {
                    var auth: FirebaseAuth = Firebase.auth

                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(postfixPhoneNumbers.value) // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(myActivity.value) // Activity (for callback binding)
                        .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(myForceResendingToken.value!!)
                        .build()

                    PhoneAuthProvider.verifyPhoneNumber(options)

                } catch (e: Exception){ }
            }
        } else {

        }
    }

    fun onChangePostfixPhoneNumbers(data: String){
        this.postfixPhoneNumbers.value = data
    }

    fun onChangeOTPCode(data: String){
        this.otpCode.value = data
    }

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d("VERIFY COMPLETE", "verificationId: ")
            verifyingProgress.value = true
            signInWithPhoneNumber(credential = credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            when (e) {
                is FirebaseNetworkException -> {
                    snackbarJobController.showSnackbar(
                        snackbarHostState = snackbarHostState.value,
                        message = "No internet connection...",
                        actionLabel = "Dismiss"
                    )
                }

                else -> {
                    snackbarJobController.showSnackbar(
                        snackbarHostState = snackbarHostState.value,
                        message = e.message.toString().substring(0, 15) + "...",
                        actionLabel = "Dismiss"
                    )
                }

            }

            verifyingProgress.value = false
        }

        override fun onCodeSent(
            verificationId: String,
            forceResendingToken: PhoneAuthProvider.ForceResendingToken
        ) {
            myVerificationId.value = verificationId
            myForceResendingToken.value = forceResendingToken

            screenState.value = SignUpState.InputOTPCode
            startTimerOTPCode()
            Log.d("OTP SENT", "verificationId: " + verificationId)
        }
    } // End callback


}