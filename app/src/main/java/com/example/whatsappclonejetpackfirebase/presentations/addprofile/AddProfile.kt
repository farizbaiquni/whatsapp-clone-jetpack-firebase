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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddProfile(navController: NavHostController){

    val addProfileViewModel: AddProfileViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val auth = Firebase.auth

    val username = addProfileViewModel.username.value
    var isCameraSelected = addProfileViewModel.isCameraSelected.value
    var imageUri = addProfileViewModel.imageUri.value
    var imageBitmap = addProfileViewModel.imageBitmap.value
    var bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                val currentUser = auth.currentUser
                if(currentUser == null){
                    navController.navigate(ScreenRoutes.SignUpScreen.route){
                        popUpTo(ScreenRoutes.SignUpScreen.route){
                            inclusive = true
                        }
                    }
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        if(imageUri == null && imageBitmap == null){
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

        imageUri?.let {
            if(!isCameraSelected){

                var tempBitmap = if(Build.VERSION.SDK_INT < 28){
                    MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    ImageDecoder.decodeBitmap(source)
                }

                tempBitmap?.let {
                    addProfileViewModel.onChangeImageUriToBitmap(tempBitmap)
                    Image(
                        bitmap = it.asImageBitmap(),
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
            }

        }

        imageBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
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
                if((imageBitmap != null || imageUri != null) && username.isNotEmpty()){
                    Toast.makeText(context, "CLICKED", Toast.LENGTH_SHORT).show()
                    var user = auth.currentUser
                    user?.let {
                        Toast.makeText(context, "CLICKED 2", Toast.LENGTH_SHORT).show()
                        addProfileViewModel.uploadImage(user.uid, context)
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary
            ),
            modifier = Modifier.padding(top = 25.dp)
        ) {
            Text(text = "NEXT", color = Color.White)
        }

        BottomSheetPhotoProfileOption(
            context = context,
            modalBottomSheetState = bottomSheetState,
            isCameraSelected = isCameraSelected,
            onImageBitmapChange = addProfileViewModel::onChangeImageBitmap,
            onIsSelectedCameraChange = addProfileViewModel::onChangeIsCameraSelected,
            onUriChange = addProfileViewModel::onChangeImageUri,
        )

    }
}


