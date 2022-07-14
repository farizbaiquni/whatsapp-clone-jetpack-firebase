package com.example.whatsappclonejetpackfirebase.presentations.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun ShimmerChat(shimmerInstance: Shimmer){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
            Row(modifier = Modifier
                .fillMaxHeight()
                .width(90.dp)
                .shimmer(shimmerInstance)
                .background(Color.Gray)){}
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp)) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .shimmer(shimmerInstance)
                    .background(Color.Gray)) {}
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(top = 10.dp)
                    .shimmer(shimmerInstance)
                    .background(Color.Gray)) {}
            }
        }
    }
}