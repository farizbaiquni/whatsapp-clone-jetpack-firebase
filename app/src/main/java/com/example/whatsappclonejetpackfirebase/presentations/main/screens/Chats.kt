package com.example.whatsappclonejetpackfirebase.presentations.main.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.whatsappclonejetpackfirebase.R
import com.example.whatsappclonejetpackfirebase.domain.model.UserProfileModel
import com.example.whatsappclonejetpackfirebase.presentations.main.ShimmerChat
import com.example.whatsappclonejetpackfirebase.utils.ImageLoader
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun Chats(
    initialLoading: Boolean,
    chatsLoading: Boolean,
    userProfileModel: UserProfileModel?
){
    val imageLoader = ImageLoader
    val gifImageLoader = imageLoader.gifImageLoader(LocalContext.current)
    val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.View)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if(chatsLoading) {
            LazyColumn {
                repeat(5) {
                    item {
                        ShimmerChat(shimmerInstance = shimmerInstance)
                    }
                }
            }
        } else {
            Text(text = "CHATS")
            Text(text = userProfileModel?.let { it.toString() } ?: "No Data")
        }
    }
}