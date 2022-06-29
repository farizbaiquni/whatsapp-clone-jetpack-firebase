package com.example.whatsappclonejetpackfirebase.presentations.addprofile

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.whatsappclonejetpackfirebase.domain.model.UserProfileModel
import com.example.whatsappclonejetpackfirebase.domain.model.UserProfileSimpleModel
import com.example.whatsappclonejetpackfirebase.utils.ErrorMessageSubString
import com.example.whatsappclonejetpackfirebase.utils.ScreenRoutes
import com.example.whatsappclonejetpackfirebase.utils.SnackbarJobController
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class AddProfileViewModel constructor(
    private val scaffoldState: ScaffoldState,
    private val snackbarJobController: SnackbarJobController,
    private val navHostController: NavHostController
    ): ViewModel() {

    private val errorMessageSubString = ErrorMessageSubString
    private val db: FirebaseFirestore = Firebase.firestore
    val screenRoutes = mutableStateOf<ScreenRoutes?>(null)
    val username = mutableStateOf("")
    val cameraImageBitmap = mutableStateOf<Bitmap?>(null)
    val galleryImageBitmap = mutableStateOf<Bitmap?>(null)
    val isCameraSelected = mutableStateOf(false)
    val isLoading = mutableStateOf<Boolean>(false)

    fun onChangeUsername(data: String){
        this.username.value = data
    }

    fun onChangeCameraImageBitmap(data: Bitmap?){
        this.cameraImageBitmap.value = data
    }

    fun onChangeGalleryImageUri(data: Bitmap?){
        this.galleryImageBitmap.value = data
    }

    fun onChangeIsCameraSelected(data: Boolean){
        this.isCameraSelected.value = data
    }

    fun uploadImage(currentUser: FirebaseUser, context: Context){
        viewModelScope.launch {
            try {
                isLoading.value = true
                //Upload Image
                val photoProfileRef = Firebase
                    .storage
                    .reference
                    .child("photoProfile/${currentUser.uid}.jpg")
                var bitmap: Bitmap? = null
                if (galleryImageBitmap.value != null) {
                    bitmap = galleryImageBitmap.value
                } else if (cameraImageBitmap.value != null) {
                    bitmap = cameraImageBitmap.value
                }

                if(bitmap != null) {
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()

                    var uploadTask = photoProfileRef.putBytes(data)
                    uploadTask.continueWithTask { task ->
                        if (!task.isSuccessful) {
                            isLoading.value = false
                            task.exception?.let {
                                snackbarJobController.showSnackbar(
                                    snackbarHostState = scaffoldState.snackbarHostState,
                                    message = errorMessageSubString.calErrorMessage(task.exception?.message.toString()),
                                    actionLabel = "Dismiss",
                                )
                                throw it
                            }
                        }
                        photoProfileRef.downloadUrl
                    }.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val downloadUri = task.result
                            updateUserPhotoProfile(currentUser, context, downloadUri)
                        }
                    }
                } else {
                    Log.d("CHECK", "uploadImage: MASUK")
                    updateUserPhotoProfile(currentUser, context, null)
                }
            } catch (e: Exception){
                isLoading.value = false
                snackbarJobController.showSnackbar(
                    snackbarHostState = scaffoldState.snackbarHostState,
                    message = errorMessageSubString.calErrorMessage(e.message.toString()),
                    actionLabel = "Dismiss",
                )
            }
        }
    }// End uploadImage


    private fun updateUserPhotoProfile(currentUser: FirebaseUser, context: Context, imageUri: Uri?){
        viewModelScope.launch {
            try {
                Log.d("CHECK", "uploadImage: MASUK UPDATE")
                val profileRef = db.collection("userProfile").document(currentUser.uid)
                val simpleProfileRef = db.collection("userSimpleProfile").document(currentUser.uid)
                val imageUrl = if (imageUri != null) imageUri.toString() else ""
                val profile = UserProfileModel(
                    idUser = currentUser.uid,
                    phoneNumbers = currentUser.phoneNumber!!,
                    photoUrl = imageUrl,
                    username = username.value,
                    about = "Hey there! Iam using WhatsApp.",
                )
                val simpleProfile = UserProfileSimpleModel(
                    idUser = currentUser.uid,
                    username = username.value,
                    photoUrl = imageUrl,
                )

                db.runTransaction { transaction ->
                    transaction.set(profileRef,  profile)
                    transaction.set(simpleProfileRef, simpleProfile)
                }.addOnSuccessListener {
                    Log.d("CHECK", "uploadImage: MASUK UPDATE SUCCESS")
                    navHostController.navigate(ScreenRoutes.MainScreen.route){
                        popUpTo(ScreenRoutes.AddProfileScreen.route){
                            inclusive = true
                        }
                    }
                }.addOnFailureListener {
                    Log.d("CHECK", "uploadImage: MASUK UPDATE FAIL")
                    isLoading.value = false
                    snackbarJobController.showSnackbar(
                        snackbarHostState = scaffoldState.snackbarHostState,
                        message = errorMessageSubString.calErrorMessage(it.message.toString()),
                        actionLabel = "Dismiss"
                    )
                }

            } catch (e: Exception){
                isLoading.value = false
                snackbarJobController.showSnackbar(
                    snackbarHostState = scaffoldState.snackbarHostState,
                    message = errorMessageSubString.calErrorMessage(e.message.toString()),
                    actionLabel = "Dismiss"
                )
            }
        }
    }


}