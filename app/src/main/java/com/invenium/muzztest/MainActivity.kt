package com.invenium.muzztest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.invenium.muzztest.data.database.MuzzDatabase
import com.invenium.muzztest.ui.chat.ChatScreen
import com.invenium.muzztest.ui.chat.ChatViewModel
import com.invenium.muzztest.ui.chat.ChatViewModelFactory

class MainActivity : ComponentActivity() {
    private val viewModel: ChatViewModel by viewModels {
        ChatViewModelFactory(
            database = MuzzDatabase.getInstance(this)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatScreen(viewModel = viewModel)
        }
    }
}
