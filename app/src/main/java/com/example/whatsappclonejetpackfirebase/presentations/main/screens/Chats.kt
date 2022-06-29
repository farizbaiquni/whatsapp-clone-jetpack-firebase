package com.example.whatsappclonejetpackfirebase.presentations.main.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.example.whatsappclonejetpackfirebase.R
import com.example.whatsappclonejetpackfirebase.domain.model.UserProfileModel
import com.example.whatsappclonejetpackfirebase.utils.ImageLoader

@Composable
fun Chats(
    initialLoading: Boolean,
    userProfileModel: UserProfileModel?
){
    val imageLoader = ImageLoader
    val gifImageLoader = imageLoader.gifImageLoader(LocalContext.current)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if(initialLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = R.drawable.loading_gif,
                    contentDescription = "loading",
                    imageLoader = gifImageLoader,
                )
            }
        } else {
            Text(text = "CHATS")
            Text(text = userProfileModel?.let { it.toString() } ?: "No Data")
        }
    }
}