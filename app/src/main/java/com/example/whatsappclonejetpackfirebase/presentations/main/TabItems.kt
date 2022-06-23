package com.example.whatsappclonejetpackfirebase.presentations.main

import androidx.compose.runtime.Composable
import com.example.whatsappclonejetpackfirebase.presentations.main.screens.Chats
import com.example.whatsappclonejetpackfirebase.presentations.main.screens.Status
import com.example.whatsappclonejetpackfirebase.presentations.main.screens.Calls

typealias ComposableFun = @Composable () -> Unit
sealed class TabItems(var title: String) {
    object Chats: TabItems(title = "CHATS")
    object Status: TabItems(title = "STATUS")
    object Calls: TabItems(title = "CALLS")
}
