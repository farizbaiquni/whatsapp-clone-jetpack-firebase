package com.example.whatsappclonejetpackfirebase.presentations.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.example.whatsappclonejetpackfirebase.R
import com.example.whatsappclonejetpackfirebase.domain.model.UserProfileModel
import com.example.whatsappclonejetpackfirebase.presentations.utils.AppViewModel
import com.example.whatsappclonejetpackfirebase.utils.ScreenRoutes
import com.google.accompanist.pager.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


@ExperimentalPermissionsApi
@OptIn(ExperimentalPagerApi::class)
@Composable
fun Main(navController: NavController, appViewModel: AppViewModel){

    val auth = Firebase.auth
    val mainViewModel: MainViewModel =  hiltViewModel()
    val tabIndex = mainViewModel.tabIndex.value

    val userProfile: UserProfileModel? = appViewModel.userProfile.value

    val lifecycleOwner = LocalLifecycleOwner.current
    val displayContactsPermissionAlert = remember { mutableStateOf(false) }
    val tabs = listOf(
        TabItems.Chats,
        TabItems.Status,
        TabItems.Calls,
    )
    val pagerState = rememberPagerState()


    val contactPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
        )
    )

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {

                val currentUser = auth.currentUser
                if(currentUser != null){
                    //Create user doc listener
                    appViewModel.userListener(currentUser.uid)

                    //Query contacts phone data
                    if(contactPermissionsState.allPermissionsGranted){
                        appViewModel.queryContacts()
                    }
                }else{
                    navController.navigate(ScreenRoutes.SignUpScreen.route){
                        popUpTo(ScreenRoutes.SignUpScreen.route){
                            inclusive = true
                        }
                    }
                }

            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    Scaffold(
        topBar = { MainTopBar() },
        floatingActionButton = {
            if(tabIndex == 0){
                FloatingActionButton(
                    onClick = {
                        if(!contactPermissionsState.allPermissionsGranted){
                            displayContactsPermissionAlert.value = true
                        }else{
                            navController.navigate(ScreenRoutes.ContactsScreen.route)
                        }
                    }) {
                    /* FAB content */
                    Icon(
                        painter =  painterResource(R.drawable.ic_baseline_message_24),
                        contentDescription = "message"
                    )
                }
            }
        },
    ) {

        ContactPermissionAlertDialog(
            displayContactsPermissionAlert.value,
            onChangeDisplayContactPermissionAlert = { displayContactsPermissionAlert.value = it },
            LocalContext.current,
        )

        Column {
            Tabs(tabs = tabs, pagerState = pagerState, mainViewModel::onChangeTabIndex)
            TabsContent(tabs = tabs, pagerState = pagerState)
        }
    }

}// Main



@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ContactPermissionAlertDialog(
    displayContactPermissionAlert: Boolean,
    onChangeDisplayContactPermissionAlert: (Boolean) -> Unit,
    context: Context,
){

    if(displayContactPermissionAlert){
        Dialog(
            onDismissRequest = { onChangeDisplayContactPermissionAlert(false) }
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(
                    Modifier
                        .background(Color.White)
                    ) {

                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colors.primary)
                            .fillMaxWidth()
                            .padding(50.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_contacts_24),
                            contentDescription = "contacts",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.padding(15.dp),
                    ) {

                        Text(text = "To help you connect with friends and family, allow WhatsApp access " +
                                "to your contacts", modifier = Modifier.padding(15.dp), textAlign = TextAlign.Justify)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            Text(
                                text = "NOT NOW",
                                color = MaterialTheme.colors.primary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .clickable {
                                        onChangeDisplayContactPermissionAlert(false)
                                    }
                            )
                            Text(
                                text = "CONTINUE",
                                color = MaterialTheme.colors.primary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                modifier = Modifier
                                    .padding(end = 15.dp)
                                    .clickable(enabled = true, onClick = {
                                        context.startActivity(Intent().apply {
                                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                            data =
                                                Uri.fromParts("package", context.packageName, null)
                                        })
                                        onChangeDisplayContactPermissionAlert(false)
                                    })
                            )
                        }

                    }
                }
            }// End surface
        }// End dialog
    }
}




@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(tabs: List<TabItems>, pagerState: PagerState, onChangeTabIndex: (Int) -> Unit) {
    val scope = rememberCoroutineScope()
    // OR ScrollableTabRow()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }) {
        tabs.forEachIndexed { index, tab ->
            // OR Tab()
            Tab(
                text = { Text(tab.title, color = Color.White, fontWeight = FontWeight.SemiBold) },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        onChangeTabIndex(index)
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }
    }
}




@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabsContent(tabs: List<TabItems>, pagerState: PagerState) {
    HorizontalPager(state = pagerState, count = tabs.size) { page ->
        tabs[page].screen()
    }
}



@Composable
fun MainTopBar() {
    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)
        ) {
            Text(text = "WhatsApp", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

