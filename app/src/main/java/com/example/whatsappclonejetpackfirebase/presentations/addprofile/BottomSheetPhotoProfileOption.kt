package com.example.whatsappclonejetpackfirebase.presentations.addprofile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.whatsappclonejetpackfirebase.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetPhotoProfileOption(
    context: Context,
    modalBottomSheetState: ModalBottomSheetState,
    isCameraSelected: Boolean,
    onIsSelectedCameraChange: (Boolean) -> Unit,
    onChangeGalleryImageUri: (Bitmap?) -> Unit,
    onChangeCameraImageBitmap: (Bitmap?) -> Unit,
){
    val coroutineScope = rememberCoroutineScope()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ){
        uri: Uri? -> run {
            if(uri != null) {
                var tempBitmap = if (Build.VERSION.SDK_INT < 28){
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                }
                onChangeCameraImageBitmap(null)
                onChangeGalleryImageUri(tempBitmap)
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ){
        bitmap: Bitmap? ->
            onChangeGalleryImageUri(null)
            onChangeCameraImageBitmap(bitmap)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ){  isGranted: Boolean ->
        if(isGranted){
            if(isCameraSelected){
                cameraLauncher.launch()
            }else{
                galleryLauncher.launch("image/*")
            }
            coroutineScope.launch {
                modalBottomSheetState.hide()
            }
        }
    }

    ModalBottomSheetLayout(
        modifier = Modifier.fillMaxSize(),
        sheetContent = {
            Column(
                modifier = Modifier
                    .padding( top = 25.dp, start = 20.dp)
            ) {

                Text(
                    text = "Profile Photo",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp,
                )

                Row(
                    Modifier.padding(vertical = 20.dp)
                ){

                    //First Option
                    Column(
                        Modifier.padding(end = 35.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        //Image
                        Column(
                            Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.LightGray.copy(0.5f), CircleShape),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_baseline_photo_camera_24),
                                contentDescription = "Camera",
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray.copy(0.5f))
                                    .clickable {
                                        when (PackageManager.PERMISSION_GRANTED) {
                                            ContextCompat.checkSelfPermission(
                                                context,
                                                Manifest.permission.CAMERA
                                            ) -> {
                                                cameraLauncher.launch()
                                                coroutineScope.launch {
                                                    modalBottomSheetState.hide()
                                                }
                                            }
                                            else -> {
                                                onIsSelectedCameraChange(true)
                                                permissionLauncher.launch(Manifest.permission.CAMERA)

                                            }
                                        }
                                    }
                            )
                        }
                        Text(text = "Camera", fontSize = 15.sp, modifier = Modifier.padding(top = 7.dp))
                    }

                    //Second Option
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        //Image container
                        Column(
                            Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.LightGray.copy(0.5f), CircleShape),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.ic_baseline_photo_24),
                                contentDescription = "Camera",
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray.copy(0.5f))
                                    .clickable {
                                        when (PackageManager.PERMISSION_GRANTED) {
                                            ContextCompat.checkSelfPermission(
                                                context,
                                                Manifest.permission.READ_EXTERNAL_STORAGE
                                            ) -> {
                                                galleryLauncher.launch("image/*")
                                                coroutineScope.launch {
                                                    modalBottomSheetState.hide()
                                                }
                                            }
                                            else -> {
                                                onIsSelectedCameraChange(false)
                                                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                                            }
                                        }
                                    }
                            )
                        }
                        Text(text = "Gallery", fontSize = 15.sp, modifier = Modifier.padding(top = 7.dp))
                    }
                }//End Row
            }// End Column
        }, //End SheetContent
        sheetState = modalBottomSheetState
    ){}
}