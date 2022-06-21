package com.example.whatsappclonejetpackfirebase.presentations.signUp

import android.text.format.DateUtils
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.whatsappclonejetpackfirebase.R
import com.example.whatsappclonejetpackfirebase.presentations.components.SnackbarComponent
import com.example.whatsappclonejetpackfirebase.utils.SnackbarJobController

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignUpOTP(
    prefixPhoneNumbers: String,
    postfixPhoneNumbers: String,
    otpCode: String,
    onChangeOTPCode: (String) -> Unit,
    otpCodeTimerLeft: Long,
    resendVerificationCode: () -> Unit,
    verifyingProgress: Boolean,
    verifyOTPCode: () -> Unit,
    snackbarHostState: SnackbarHostState,
    snackbarJobController: SnackbarJobController,
){

    val keyboardController = LocalSoftwareKeyboardController.current
    val backgroundColor = remember { mutableStateOf(Color.LightGray) }

    LaunchedEffect(key1 = otpCodeTimerLeft, block = {
        if (otpCodeTimerLeft <= 0) {
            backgroundColor.value = Color.Transparent
        } else {
            backgroundColor.value = Color.LightGray
        }
    } )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
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
                    modifier = Modifier.padding(top = 20.dp),
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
                        value = otpCode,
                        onValueChange = {
                            if(it.trim().length <= 6){
                                onChangeOTPCode(it.trim())
                            }
                        },
                        placeholder = {
                            Text(
                                text = "_ _ _   _ _ _",
                                modifier = Modifier.fillMaxWidth(),
                                style = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                            )
                        },
                        maxLines = 1,
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            backgroundColor = Color.Transparent,
                        ),

                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),

                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            },
                        ),
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
                                    resendVerificationCode()
                                }
                            }
                        )
                        .background(
                            color = backgroundColor.value
                        )
                        .clip(RoundedCornerShape(15f)),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row {
                        Image(
                            painterResource(id = R.drawable.ic_baseline_message_24),
                            contentDescription = "message icon",
                            modifier = Modifier
                                .padding(end = 10.dp)
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
                        .background(
                            color = backgroundColor.value
                        )
                        .clip(RoundedCornerShape(15f)),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painterResource(id = R.drawable.ic_baseline_call_24),
                            contentDescription = "call icon",
                            modifier = Modifier
                                .padding(end = 10.dp)
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

            }
            Button(
                onClick = {
                    if(otpCode.length < 6){
                        snackbarJobController.showSnackbar(
                            snackbarHostState = snackbarHostState,
                            message = "Please enter 6 digit code",
                            actionLabel = "Dismiss",
                        )
                    } else { verifyOTPCode() }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(R.color.green_500)
                ),
                shape = RoundedCornerShape(3.dp),
            ) {
                Text(text = "Next", color = Color.White)
            }
        } // End Column

        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            SnackbarComponent(
                snackbarHostState = snackbarHostState
            )
        }

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
}// End composable


fun formatMinutesSecondsDuration(seconds: Long): String = if (seconds < 60) {
    seconds.toString()
} else {
    DateUtils.formatElapsedTime(seconds)
}