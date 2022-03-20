package com.example.whatsappclonejetpackfirebase.presentations.signUp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whatsappclonejetpackfirebase.ui.theme.WhatsappCloneJetpackFirebaseTheme

@Composable
fun SignUp(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp, horizontal = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally,) {
            Text(
                text = "Verify your phone number",
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.SemiBold,
                fontStyle = MaterialTheme.typography.h1.fontStyle,
                fontSize = 20.sp,
            )

            Text(
                text = "WhatsApp will send an SMS message (carrier changes may apply) " +
                        "to verify your phone number. Enter your country code and phone number",
                fontStyle = MaterialTheme.typography.body1.fontStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 10.dp),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .drawBehind {
                        val strokeWidth = 1 * density
                        val y = size.height - strokeWidth / 1

                        drawLine(
                            Color(0xFF128c7e),
                            Offset(0f, y + 25f),
                            Offset(size.width, y + 25f),
                            strokeWidth
                        )
                    }
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Indonesia", fontSize = 17.sp)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.25f)
                        .drawBehind {
                            val strokeWidth = 1 * density
                            val y = size.height - strokeWidth / 1

                            drawLine(
                                Color(0xFF128c7e),
                                Offset(0f, y + 10f),
                                Offset(size.width, y + 10f),
                                strokeWidth
                            )
                        },
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextField(
                        value = "+64",
                        onValueChange = {},
                        maxLines = 1,
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            backgroundColor = Color.Transparent,
                        )
                    )
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            val strokeWidth = 1 * density
                            val y = size.height - strokeWidth / 1

                            drawLine(
                                Color(0xFF128c7e),
                                Offset(0f + 30f, y + 10f),
                                Offset(size.width + 30f, y + 10f),
                                strokeWidth
                            )
                        },
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextField(
                        value = "",
                        onValueChange = {},
                        maxLines = 1,
                        placeholder = { Text(text = "phone number")},
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            backgroundColor = Color.Transparent,
                        )
                    )
                }
            } // End Row
        }
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = { },
            ) {
                Text(text = "NEXT", color = Color.White)
            }
            Text(
                text = "Carrier SMS charges may apply",
                fontStyle = MaterialTheme.typography.h6.fontStyle,
                modifier = Modifier.padding(top = 10.dp)
            )
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