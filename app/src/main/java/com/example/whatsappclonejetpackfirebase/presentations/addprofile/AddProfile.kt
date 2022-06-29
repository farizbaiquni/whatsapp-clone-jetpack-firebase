package com.example.whatsappclonejetpackfirebase.presentations.addprofile

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.whatsappclonejetpackfirebase.R
import com.example.whatsappclonejetpackfirebase.presentations.components.SnackbarComponent
import com.example.whatsappclonejetpackfirebase.utils.ScreenRoutes
import com.example.whatsappclonejetpackfirebase.utils.SnackbarJobController
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddProfile(
    navHostController: NavHostController,
){
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val snackbarJobController = SnackbarJobController(scope)
    val addProfileViewModel: AddProfileViewModel = viewModel(
        factory = AddProfileViewModelFactory(
            scaffoldState = scaffoldState,
            snackbarJobController = snackbarJobController,
            navHostController = navHostController,
        )
    )

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

    val keyboardController = LocalSoftwareKeyboardController.current

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
            navHostController.navigate(ScreenRoutes.SignUpScreen.route){
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
                    navHostController.navigate(ScreenRoutes.MainScreen.route){
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
    
    Scaffold() { it ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

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
                                scope.launch {
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
                                    scope.launch {
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
                                scope.launch {
                                    bottomSheetState.show()
                                }
                            }
                    )
                }

                TextField(
                    value = username,
                    onValueChange = {
                        if(it.length <= 25) {
                            addProfileViewModel.onChangeUsername(it)
                        }
                    },
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = MaterialTheme.colors.primary,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.LightGray,
                        backgroundColor = Color.Transparent,
                    ),
                    placeholder = { Text(text = "Type your name here")},
                    modifier = Modifier.padding(top = 25.dp, start = 10.dp, end = 10.dp),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    ),
                )

                Button(
                    onClick = {
                        authUser.value?.let {
                            if(username.length >= 4) {
                                addProfileViewModel.uploadImage(it, context)
                            } else {
                                snackbarJobController.showSnackbar(
                                    snackbarHostState = scaffoldState.snackbarHostState,
                                    message = "Username must be at least 4 characters in length",
                                    actionLabel = "Dismiss"
                                )
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

            if(isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.6f)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Column(modifier = Modifier.clip(RoundedCornerShape(5.dp))) {
                        Row(
                            modifier = Modifier
                                .background(Color.White)
                                .padding(vertical = 9.dp, horizontal = 13.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = R.drawable.loading_gif,
                                imageLoader = imageLoader,
                                contentDescription = "loading",
                                modifier = Modifier.size(45.dp)
                            )
                            Text(
                                text = "Processing, Please wait...",
                                modifier = Modifier.padding(start = 10.dp)
                            )
                        }
                    }
                }
            }

            Column(modifier = Modifier.align(Alignment.BottomCenter).padding(horizontal = 10.dp).padding(bottom = 10.dp)) {
                SnackbarComponent(snackbarHostState = scaffoldState.snackbarHostState)
            }

        } // End Box

    }
}




