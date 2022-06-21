package com.example.whatsappclonejetpackfirebase.presentations.utils

import android.content.Context
import android.os.Build
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder

fun gifImageLoader(context: Context): ImageLoader{
    val imageLoader = ImageLoader.Builder(context).components() {
        if (Build.VERSION.SDK_INT >= 28) {
            add(ImageDecoderDecoder.Factory())
        } else {
            add(GifDecoder.Factory())
        }
    }.build()
    return imageLoader
}