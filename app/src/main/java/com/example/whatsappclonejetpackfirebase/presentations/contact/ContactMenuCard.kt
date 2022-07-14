package com.example.whatsappclonejetpackfirebase.presentations.contact

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whatsappclonejetpackfirebase.R

@Composable
fun ContactMenuCard(
    name: String,
    image: Painter,
    paddingTop: Dp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = paddingTop)
    ) {

        Image(
            painter = image,
            contentDescription = name,
            Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary)
                .padding(7.dp)
        )

        Text(
            text = name,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 20.dp),
            fontSize = 15.sp,
        )
    }// End Row
}