package com.example.whatsappclonejetpackfirebase.presentations.main

import androidx.compose.runtime.Composable
import com.example.whatsappclonejetpackfirebase.presentations.main.screens.Chats
import com.example.whatsappclonejetpackfirebase.presentations.main.screens.Status
import com.example.whatsappclonejetpackfirebase.presentations.main.screens.Calls

typealias ComposableFun = @Composable () -> Unit
sealed class TabItems(var title: String, var screen: ComposableFun) {
    object Chats: TabItems(title = "CHATS", { Chats() })
    object Status: TabItems(title = "STATUS", { Status() })
    object Calls: TabItems(title = "CALLS", { Calls() })
}
