package com.example.whatsappclonejetpackfirebase.presentations.addprofile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class AddProfileViewModel @Inject constructor(): ViewModel() {

    val username = mutableStateOf("")
    val imageBitmap = mutableStateOf<Bitmap?>(null)
    val imageUri = mutableStateOf<Uri?>(null)
    val isCameraSelected = mutableStateOf(false)
    val imageUriToBitmap = mutableStateOf<Bitmap?>(null)

    fun onChangeUsername(data: String){
        this.username.value = data
    }

    fun onChangeImageBitmap(data: Bitmap?){
        this.imageBitmap.value = data
    }

    fun onChangeImageUri(data: Uri?){
        this.imageUri.value = data
    }

    fun onChangeIsCameraSelected(data: Boolean){
        this.isCameraSelected.value = data
    }

    fun onChangeImageUriToBitmap(data: Bitmap){
        this.imageUriToBitmap.value = data
    }

    fun uploadImage(idUser: String, context: Context){
        viewModelScope.launch {
            Toast.makeText(context, "INSIDE UPLOAD IMAGE PROGRESS", Toast.LENGTH_SHORT).show()
            try {
                //Upload Image
                val photoProfileRef = Firebase
                    .storage
                    .reference
                    .child("photoProfile/${idUser}.jpg")

                var bitmap: Bitmap? = null
                if(imageUri.value != null){
                    bitmap = imageUriToBitmap.value
                } else if(imageBitmap.value != null){
                    bitmap = imageBitmap.value
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
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()}
        }
    }// End uploadImage


    fun updateUserPhotoProfile(context: Context, imageUri: Uri){
        viewModelScope.launch {
            try {
                val user = Firebase.auth.currentUser

                val profileUpdates = userProfileChangeRequest {
                    photoUri = imageUri
                }

                user!!.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Photo profile changed", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Change photo profile failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            } catch (e: Exception){
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }


}