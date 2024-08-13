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
import androidx.compose.ui.Modifier
import com.invenium.muzztest.data.local.entity.Message
import com.invenium.muzztest.ui.theme.MuzzTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MuzzTestTheme {
                Scaffold(
                    topBar = { TopAppBar(title = { Text("Muzz Chat") }) },
                    bottomBar = { BottomAppBar(content = { Text("Send") }) }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        MessageList()
                        TextEntryBox()
                    }
                }
            }
        }
    }
}

@Composable
fun MessageList() {
    LazyColumn(reverseLayout = true) {
        val sampleMessages = listOf(
            Message("User 1", "Hello!", System.currentTimeMillis() - 10000),
            Message("User 2", "Hi there!", System.currentTimeMillis() - 8000),
            Message("User 1", "How are you?", System.currentTimeMillis() - 6000),
            Message("User 2", "I'm doing well, thanks!", System.currentTimeMillis() - 4000)
        )
        items(sampleMessages) { message ->
            MessageBubble(message)
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
    Text(text = "${message.sender}: ${message.content}")
}

@Composable
fun TextEntryBox() {
    Text(text = "Text Entry Box")
}
