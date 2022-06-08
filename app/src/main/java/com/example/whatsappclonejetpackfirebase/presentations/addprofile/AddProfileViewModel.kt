package com.example.whatsappclonejetpackfirebase.presentations.addprofile

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatsappclonejetpackfirebase.domain.model.UserProfileModel
import com.example.whatsappclonejetpackfirebase.domain.model.UserProfileSimpleModel
import com.example.whatsappclonejetpackfirebase.utils.ScreenRoutes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AddProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
): ViewModel() {

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
            Toast.makeText(context, "INSIDE UPLOAD IMAGE PROGRESS", Toast.LENGTH_SHORT).show()
            try {
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
                            updateUserPhotoProfile(currentUser, context, downloadUri)
                        } else {
                            Toast.makeText(context, "Upload image failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: Exception){
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }// End uploadImage


    private fun updateUserPhotoProfile(currentUser: FirebaseUser, context: Context, imageUri: Uri){
        viewModelScope.launch {
            try {
                val profileRef = db.collection("userProfile").document(currentUser.uid)
                val simpleProfileRef = db.collection("userSimpleProfile").document(currentUser.uid)
                val profile = UserProfileModel(
                    idUser = currentUser.uid,
                    phoneNumbers = currentUser.phoneNumber!!,
                    photoUrl = imageUri.toString(),
                    username = username.value,
                    about = "Hey there! Iam using WhatsApp.",
                )
                val simpleProfile = UserProfileSimpleModel(
                    idUser = currentUser.uid,
                    username = username.value,
                    photoUrl = imageUri.toString(),
                )

                db.runBatch { batch ->
                    batch.set(profileRef,  profile)
                    batch.set(simpleProfileRef, simpleProfile)
                }.addOnSuccessListener {

                }.addOnFailureListener {
                    Toast.makeText(context, "Failed add profile: $it", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception){
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }


}