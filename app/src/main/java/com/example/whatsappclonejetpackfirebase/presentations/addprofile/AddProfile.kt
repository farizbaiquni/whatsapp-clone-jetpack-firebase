package com.example.whatsappclonejetpackfirebase.presentations.addprofile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.whatsappclonejetpackfirebase.R

@Composable
fun AddProfile(){

    val addProfileViewModel: AddProfileViewModel = hiltViewModel()
    val username = addProfileViewModel.username.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_baseline_add_a_photo_24),
            contentDescription = "add photo",
            modifier = Modifier
                .size(85.dp)
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha = 0.3f))
                .padding(20.dp)
        )

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
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary
            ),
            modifier = Modifier.padding(top = 25.dp)
        ) {
            Text(text = "NEXT", color = Color.White)
        }

    }
}