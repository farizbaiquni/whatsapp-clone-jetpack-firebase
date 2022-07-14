package com.example.whatsappclonejetpackfirebase.presentations.contact

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.whatsappclonejetpackfirebase.R

@Composable
fun ContactAccountCard(
    photoUrl: String,
    name: String,
    about: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 20.dp)
    ) {

        if(photoUrl.isBlank()){
            Image(
                painter = painterResource(R.drawable.ic_baseline_person_24),
                contentDescription = "photo profile",
                Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary)
                    .padding(7.dp)
            )
        } else {
            AsyncImage(
                model = photoUrl,
                contentDescription = "photo profile",
                placeholder = painterResource(R.drawable.ic_baseline_person_24),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
        }

        Column(){
            Text(
                text = name,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 20.dp),
                fontSize = 15.sp,
            )
            Text(
                text = about,
                modifier = Modifier.padding(start = 20.dp),
                fontSize = 13.sp,
            )
        }
    }// End Row
}