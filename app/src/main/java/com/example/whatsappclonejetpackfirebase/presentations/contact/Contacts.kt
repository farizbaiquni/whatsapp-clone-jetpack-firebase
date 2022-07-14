package com.example.whatsappclonejetpackfirebase.presentations.contact

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.whatsappclonejetpackfirebase.R
import com.example.whatsappclonejetpackfirebase.utils.PermissionHelper
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@ExperimentalPermissionsApi
@Composable
fun Contacts(navController: NavController){

    val contactsViewModel: ContactsViewModel = hiltViewModel()
    val permissionHelper = PermissionHelper()
    val contactModelPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
        )
    )
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val contacts = contactsViewModel.contacts.value
    val contactsAccount = contactsViewModel.contactsAccount.value

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

    LaunchedEffect(key1 = contacts, block = {
        if(contacts.isNotEmpty() && contactsAccount.size < 10) {
            contactsViewModel.initQueryUserProfileFromContacts()
        }
    })

    Scaffold(
        topBar = { ContactsTopBar(navController) },
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 15.dp, vertical = 15.dp)
        ) {

            item {
                ContactMenuCard(
                    name = "New Group",
                    image = painterResource(R.drawable.ic_baseline_group_24),
                    paddingTop = 0.dp
                )
            }

            item {
                ContactMenuCard(
                    name = "New Contact",
                    image = painterResource(R.drawable.ic_baseline_person_add_24),
                    paddingTop = 20.dp
                )
            }

            items(
                items = contactsAccount,
                key = { account ->
                    // Return a stable + unique key for the item
                    account.phoneNumber
                }
            ) { data ->
                ContactAccountCard(
                    photoUrl = data.photoUrl,
                    name = contacts.get(data.phoneNumber).toString(),
                    about = data.about
                )
            }

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