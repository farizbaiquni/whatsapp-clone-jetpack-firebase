package com.example.whatsappclonejetpackfirebase.presentations.signUp

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.whatsappclonejetpackfirebase.ui.theme.WhatsappCloneJetpackFirebaseTheme

@Composable
fun SignUp(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp, horizontal = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Verify your phone number",
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.SemiBold,
            fontStyle = MaterialTheme.typography.h1.fontStyle,
        )

        Text(
            text = "WhatsApp will send an SMS message (carrier changes may apply) " +
                    "to verify your phone number. Enter your country code and phone number",
            fontStyle = MaterialTheme.typography.body1.fontStyle,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp),
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(0.75f).drawBehind {
                val strokeWidth = 1 * density
                val y = size.height - strokeWidth / 2

                drawLine(
                    Color(0xFF128c7e),
                    Offset(0f, y),
                    Offset(size.width, y),
                    strokeWidth
                )
            }.padding(top = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Indonesia")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpPreview() {
    WhatsappCloneJetpackFirebaseTheme {
        SignUp()
    }
}