package com.invenium.muzztest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.* // ktlint-disable no-wildcard-imports
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.invenium.muzztest.data.local.entity.Message
import com.invenium.muzztest.ui.chat.components.TextEntryBox
import com.invenium.muzztest.ui.theme.MuzzTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MuzzTestTheme {
                val messages = remember { mutableStateListOf<Message>() }
                Scaffold(
                    topBar = { TopAppBar(title = { Text("Muzz Chat") }) },
                    bottomBar = { BottomAppBar(content = { Text("Send") }) }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        MessageList(messages = messages)
                        TextEntryBox(messages = messages, onMessageSent = { messageText ->
                            messages.add(Message("User 1", messageText, System.currentTimeMillis()))
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun MessageList(messages: List<Message>) {
    LazyColumn(reverseLayout = true) {
        items(messages) { message ->
            MessageBubble(message)
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
    Text(text = "${message.sender}: ${message.content}")
}
