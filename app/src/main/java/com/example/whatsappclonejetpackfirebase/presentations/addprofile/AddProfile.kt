package com.example.whatsappclonejetpackfirebase.presentations.addprofile

import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.example.whatsappclonejetpackfirebase.R
import com.example.whatsappclonejetpackfirebase.utils.ScreenRoutes
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddProfile(
    navController: NavHostController,
){
    val addProfileViewModel: AddProfileViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val auth = Firebase.auth

    val authUser = remember { mutableStateOf<FirebaseUser?>(null) }
    val username = addProfileViewModel.username.value
    var isCameraSelected = addProfileViewModel.isCameraSelected.value
    var cameraImageBitmap = addProfileViewModel.cameraImageBitmap.value
    var galleryImageUri = addProfileViewModel.galleryImageUri.value
    var bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                val currentUser = auth.currentUser
                if (currentUser == null){
                    navController.navigate(ScreenRoutes.SignUpScreen.route){
                        popUpTo(ScreenRoutes.AddProfileScreen.route){
                            inclusive = true
                        }
                    }
                } else {
                    authUser.value = currentUser
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            if(galleryImageUri == null && cameraImageBitmap == null){
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_add_a_photo_24),
                    contentDescription = "add photo",
                    modifier = Modifier
                        .size(85.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray.copy(alpha = 0.3f))
                        .padding(20.dp)
                        .clickable {
                            coroutineScope.launch {
                                bottomSheetState.show()
                            }
                        }
                )
            }

            galleryImageUri?.let {
                if(!isCameraSelected){
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Photo Profile",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .size(85.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray.copy(alpha = 0.3f))
                            .clickable {
                                coroutineScope.launch {
                                    bottomSheetState.show()
                                }
                            }
                    )
                }
            }

            cameraImageBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = "Photo Profile",
                    modifier = Modifier
                        .size(85.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray.copy(alpha = 0.3f))
                        .clickable {
                            coroutineScope.launch {
                                bottomSheetState.show()
                            }
                        }
                )
            }

            TextField(
                value = username,
                onValueChange = {addProfileViewModel.onChangeUsername(it)},
                maxLines = 1,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = MaterialTheme.colors.primary,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.LightGray,
                    backgroundColor = Color.Transparent,
                ),
                placeholder = { Text(text = "Type your name here")},
                modifier = Modifier.padding(top = 25.dp)
            )

            Button(
                onClick = {
                    if(
                        (cameraImageBitmap != null || galleryImageUri != null) &&
                        username.isNotEmpty() && authUser != null
                    ){
                        addProfileViewModel.uploadImage(authUser.value?.uid, context)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary
                ),
                modifier = Modifier.padding(top = 25.dp)
            ) {
                Text(text = "NEXT", color = Color.White)
            }
        } // End Column

        BottomSheetPhotoProfileOption(
            context = context,
            modalBottomSheetState = bottomSheetState,
            isCameraSelected = isCameraSelected,
            onChangeCameraImageBitmap = addProfileViewModel::onChangeCameraImageBitmap,
            onIsSelectedCameraChange = addProfileViewModel::onChangeIsCameraSelected,
            onChangeGalleryImageUri = addProfileViewModel::onChangeGalleryImageUri,
        )
    } // End Box

}




