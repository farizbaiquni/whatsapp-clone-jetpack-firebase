package com.example.whatsappclonejetpackfirebase.presentations.contact

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.whatsappclonejetpackfirebase.R
import com.example.whatsappclonejetpackfirebase.presentations.utils.AppViewModel
import com.example.whatsappclonejetpackfirebase.utils.PermissionHelper
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@ExperimentalPermissionsApi
@Composable
fun Contacts(navController: NavController, appViewModel: AppViewModel){

    val permissionHelper = PermissionHelper()
    val contactModelPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
        )
    )
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if(event == Lifecycle.Event.ON_CREATE){
                    permissionHelper.multiplePermissions(contactModelPermissionsState)
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )// DisposableEffect


    Scaffold(
        topBar = { ContactsTopBar(navController) },
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 15.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(R.drawable.ic_baseline_group_24),
                    contentDescription = "group",
                    Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.primary)
                        .padding(7.dp)
                )

                Text(
                    text = "New group",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 15.dp),
                    fontSize = 18.sp,
                )
            }// End Row

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 20.dp)
            ) {

                Image(
                    painter = painterResource(R.drawable.ic_baseline_person_add_24),
                    contentDescription = "group",
                    Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.primary)
                        .padding(7.dp)
                )

                Text(
                    text = "New contact",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 15.dp),
                    fontSize = 18.sp,
                )
            }// End Row

        }// End Column

    }
}


@Composable
fun ContactsTopBar(navController: NavController){
    TopAppBar(
        title = {
            Text(text = "Select Contact")
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White,
        elevation = 10.dp
    )
}