package com.example.whatsappclonejetpackfirebase.presentations.signUp

import android.app.Activity
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class SignUpViewModel(activity: Activity): ViewModel() {

    val prefixPhoneNumbers = mutableStateOf("+62")
    val postfixPhoneNumbers = mutableStateOf("")
    val otpCodeTimerLeft = mutableStateOf<Long>(65)
    val otpCode = mutableStateOf("")
    val myActivity: MutableState<Activity> = mutableStateOf(activity)
    val myForceResendingToken: MutableState<PhoneAuthProvider.ForceResendingToken?> = mutableStateOf(null)
    val myVerificationId: MutableState<String?> = mutableStateOf(null)
    val error: MutableState<Exception?> = mutableStateOf(null)
    val screenState: MutableState<SignUpState> = mutableStateOf(SignUpState.InputPhoneNumber)
    val verifyingProgress = mutableStateOf(false)
    val isVerificationSuccess = mutableStateOf(false)
    private val db = Firebase.firestore

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
            val credential = PhoneAuthProvider.getCredential(myVerificationId.value!!, otpCode.value)
            signInWithPhoneNumber(credential)
        } catch (e: Exception){ }
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
                    val user = task.result?.user
                    Log.d("LOGIN ", "SUCCESS")

                    user?.let {
                        addAndUpdateUserData(user)
                    }
                    verifyingProgress.value = false
                } else {
                    // Sign in failed, display a message and update the UI
                    verifyingProgress.value = false
                    this.screenState.value = SignUpState.InputPhoneNumber
                }
        }
    } // End signInWithPhoneNumber


    fun addAndUpdateUserData(user: FirebaseUser){
        val sfDocRef = db.collection("users").document(user.uid)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(sfDocRef)

            // Update user auth profile
            user.updateEmail("0" + postfixPhoneNumbers.value)

            // Add data user
            if(!snapshot.exists()){
                Log.d("UPDATING DATA", "addAndUpdateUserData: ")
                val data = hashMapOf(
                    "idUser" to user.uid,
                    "phoneNumber" to "0" + postfixPhoneNumbers.value,
                    "photoUrl" to null,
                    "username" to null,
                    "about" to "Hi there! I am using whatsapp",
                )
                transaction.set(sfDocRef, data)
            }
            // Success
            null
        }.addOnSuccessListener {
            onChangeIsVerificationSuccess(true)
        }.addOnFailureListener {
            Log.d("FAILED UPDATING DATA", it.toString())
            Firebase.auth.signOut()
            this.screenState.value = SignUpState.InputPhoneNumber
        }

    }// End addUserData


    fun resendVerificationCode() {
        if(myForceResendingToken.value != null){
            viewModelScope.launch {
                try {
                    var auth: FirebaseAuth = Firebase.auth

                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(postfixPhoneNumbers.value)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(myActivity.value)                 // Activity (for callback binding)
                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
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

    fun onChangeIsVerificationSuccess(data: Boolean){
        this.isVerificationSuccess.value = data
    }

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d("VERIFY COMPLETE", "verificationId: ")
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            verifyingProgress.value = true
            signInWithPhoneNumber(credential = credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                error.value = e
                Log.d("FAILED INVALID REQUEST", e.toString())
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                error.value = e
                Log.d("FAILED QOUTA EXCEEDED", e.toString())
            }

            Log.d("VERIFICATION FAILED", e.toString())
            verifyingProgress.value = false

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            forceResendingToken: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            // Log.d(TAG, "onCodeSent:$verificationId")

            screenState.value = SignUpState.InputOTPCode
            startTimerOTPCode()

            // Save verification ID and resending token so we can use them later
            myVerificationId.value = verificationId
            myForceResendingToken.value = forceResendingToken
            Log.d("OTP SENT", "verificationId: " + verificationId)
        }
    } // End callback


}