package com.example.whatsappclonejetpackfirebase.presentations.retry

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.whatsappclonejetpackfirebase.presentations.utils.gifImageLoader
import com.example.whatsappclonejetpackfirebase.R

@Composable
fun Retry(
    errorAction: () -> Unit,
    errorMessage: String,
    loading: Boolean
){
    val context = LocalContext.current
    val gifImageLoader = gifImageLoader(context)
    Box() {
        if (!loading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = errorMessage,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 15.dp),
                    color = MaterialTheme.colors.error
                )
                Button(
                    onClick = errorAction,
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Text(text = "Retry")
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = R.drawable.loading_gif,
                    contentDescription = "loading",
                    imageLoader = gifImageLoader,
                    modifier = Modifier.size(55.dp)
                )
            }
        }

    }
}