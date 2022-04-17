package com.example.whatsappclonejetpackfirebase.presentations.signUp

import android.text.format.DateUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.whatsappclonejetpackfirebase.R

@Composable
fun SignUpOTP(
    prefixPhoneNumbers: String,
    postfixPhoneNumbers: String,
    otpCode: String,
    onChangeOTPCode: (String) -> Unit,
    otpCodeTimerLeft: Long,
    resendVerificationCode: () -> Unit,
    verifyingProgress: Boolean,
){

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Verify ${prefixPhoneNumbers} ${postfixPhoneNumbers}",
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.SemiBold,
            fontStyle = MaterialTheme.typography.h1.fontStyle,
            fontSize = 20.sp,
        )

        Text(
            text = "Waiting to automatically detect an SMS to ${prefixPhoneNumbers} " +
                    "${postfixPhoneNumbers} Wrong number",
            fontStyle = MaterialTheme.typography.body1.fontStyle,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp),
        )

        Row(
            Modifier
                .width(200.dp)
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
            horizontalArrangement = Arrangement.Center,
        ) {
            TextField(
                value = "_ _ _     _ _ _",
                onValueChange = {
                    if(otpCode.length < 6){
                        onChangeOTPCode(it.trim())
                    }
                },
                readOnly = true,
                maxLines = 1,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
            )
        }

        Text(
            text = "Enter 6-digit code",
            modifier = Modifier.padding(top = 11.dp),
            fontSize = 15.sp
        )
        
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth(0.90f)
                .clickable(
                    onClick = {
                        if (otpCodeTimerLeft <= 0) {
                            resendVerificationCode
                        }
                    }
                )
                .clip(RoundedCornerShape(15f)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row {
                Image(
                    painterResource(id = R.drawable.ic_baseline_message_24),
                    contentDescription = "message icon",
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .padding(start = 10.dp)
                )
                Text(text = "Resend SMS")
            }
            Text(
                text = formatMinutesSecondsDuration(otpCodeTimerLeft),
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 10.dp)
                    .padding(end = 10.dp)
            )
        }

        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier
                .padding(top = 7.5.dp)
                .fillMaxWidth(0.85f),
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(top = 7.5.dp)
                .fillMaxWidth(0.90f)
                .clip(RoundedCornerShape(15f)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painterResource(id = R.drawable.ic_baseline_call_24),
                    contentDescription = "call icon",
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .padding(start = 10.dp)
                )
                Text(
                    text = "Call Me",
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .padding(end = 10.dp)
                )
            }

            Text(
                text = formatMinutesSecondsDuration(otpCodeTimerLeft),
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 10.dp)
                    .padding(end = 10.dp)
            )
        }
    } // End Row


    if(verifyingProgress) {
        Dialog(
            onDismissRequest = { /*TODO*/ },
            content = {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.White),
                ) {
                    Row(
                        Modifier.padding(vertical = 20.dp, horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.padding(start = 17.dp)
                        )
                        Text(
                            text = "Verifying....",
                            fontSize = 17.sp,
                            modifier = Modifier.padding(start = 15.dp)
                        )
                    }
                }
            },
        )// End Dialog
    } // End if
}


fun formatMinutesSecondsDuration(seconds: Long): String = if (seconds < 60) {
    seconds.toString()
} else {
    DateUtils.formatElapsedTime(seconds)
}