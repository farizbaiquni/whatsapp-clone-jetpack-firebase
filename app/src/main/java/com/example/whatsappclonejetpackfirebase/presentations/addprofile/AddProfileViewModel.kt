package com.example.whatsappclonejetpackfirebase.presentations.addprofile

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AddProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth
): ViewModel() {

    val username = mutableStateOf("")
    val cameraImageBitmap = mutableStateOf<Bitmap?>(null)
    val galleryImageUri = mutableStateOf<Bitmap?>(null)
    val galleryImageUriToBitmap = mutableStateOf<Bitmap?>(null)
    val isCameraSelected = mutableStateOf(false)
    val isLoading = mutableStateOf<Boolean>(false)

    fun onChangeUsername(data: String){
        this.username.value = data
    }

    fun onChangeCameraImageBitmap(data: Bitmap?){
        this.cameraImageBitmap.value = data
    }

    fun onChangeGalleryImageUri(data: Bitmap?){
        this.galleryImageUri.value = data
    }

    fun onChangeIsCameraSelected(data: Boolean){
        this.isCameraSelected.value = data
    }

    fun uploadImage(paramIdUser: String?, context: Context){
        viewModelScope.launch {
            var idUser: String? = null
            if (paramIdUser == null) {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    idUser = currentUser.uid
                }
            } else {
                idUser = paramIdUser
            }
            if (idUser != null) {
                Toast.makeText(context, "INSIDE UPLOAD IMAGE PROGRESS", Toast.LENGTH_SHORT).show()
                try {
                    //Upload Image
                    val photoProfileRef = Firebase
                        .storage
                        .reference
                        .child("photoProfile/${idUser}.jpg")

                    var bitmap: Bitmap? = null
                    if (galleryImageUri.value != null) {
                        bitmap = galleryImageUriToBitmap.value
                    } else if (cameraImageBitmap.value != null) {
                        bitmap = cameraImageBitmap.value
                    }

                    bitmap?.let {
                        val baos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val data = baos.toByteArray()

                        var uploadTask = photoProfileRef.putBytes(data)
                        uploadTask.continueWithTask { task ->
                            if (!task.isSuccessful) {
                                task.exception?.let {
                                    throw it
                                }
                            }
                            photoProfileRef.downloadUrl
                        }.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val downloadUri = task.result
                                updateUserPhotoProfile(context, downloadUri)
                            } else {
                                Toast.makeText(context, "Upload image failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } catch (e: Exception){
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }// End uploadImage


    fun updateUserPhotoProfile(context: Context, imageUri: Uri){
        viewModelScope.launch {
            try {

            } catch (e: Exception){
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }


}