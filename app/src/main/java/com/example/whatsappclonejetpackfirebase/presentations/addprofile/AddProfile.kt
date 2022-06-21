package com.example.whatsappclonejetpackfirebase.presentations.addprofile

import android.os.Build.VERSION.SDK_INT
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
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
    var screenRoutes = addProfileViewModel.screenRoutes.value
    var isCameraSelected = addProfileViewModel.isCameraSelected.value
    var cameraImageBitmap = addProfileViewModel.cameraImageBitmap.value
    var galleryImageBitmap = addProfileViewModel.galleryImageBitmap.value
    var bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var lifecycleOwner = LocalLifecycleOwner.current
    val isLoading = addProfileViewModel.isLoading.value

    val imageLoader = ImageLoader.Builder(context).components() {
        if (SDK_INT >= 28) {
            add(ImageDecoderDecoder.Factory())
        } else {
            add(GifDecoder.Factory())
        }
    }.build()

    fun checkAuthUser(){
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

    LaunchedEffect(key1 = screenRoutes){
        if (screenRoutes != null) {
            when (screenRoutes ){
                is ScreenRoutes.MainScreen -> {
                    navController.navigate(ScreenRoutes.MainScreen.route){
                        popUpTo(ScreenRoutes.AddProfileScreen.route){
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                checkAuthUser()
            } else if(event == Lifecycle.Event.ON_RESUME) {
                checkAuthUser()
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
        if(!isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                if(galleryImageBitmap == null && cameraImageBitmap == null){
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

                galleryImageBitmap?.let {
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
                            (cameraImageBitmap != null || galleryImageBitmap != null) &&
                            username.isNotEmpty()
                        ){
                            authUser.value?.let {
                                addProfileViewModel.uploadImage(it, context)
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
            } // End Column

            BottomSheetPhotoProfileOption(
                context = context,
                modalBottomSheetState = bottomSheetState,
                isCameraSelected = isCameraSelected,
                onChangeCameraImageBitmap = addProfileViewModel::onChangeCameraImageBitmap,
                onIsSelectedCameraChange = addProfileViewModel::onChangeIsCameraSelected,
                onChangeGalleryImageBitmap = addProfileViewModel::onChangeGalleryImageUri,
            )
        }

        Column(
            modifier = Modifier.fillMaxSize().background(Color.Gray),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                modifier = Modifier.background(Color.White)
                    .padding(vertical = 7.dp, horizontal = 13.dp),
                verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = R.drawable.loading_gif,
                    imageLoader = imageLoader,
                    contentDescription = "loading",
                    modifier = Modifier.size(55.dp)
                )
                Text(text = "Processing, Please wait...", modifier = Modifier.padding(start = 7.dp))
            }
        }
    } // End Box

}




