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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.invenium.muzztest.data.local.entity.Message
import com.invenium.muzztest.ui.chat.components.TextEntryBox
import com.invenium.muzztest.ui.theme.MuzzTestTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MuzzTestTheme {
                val messages = remember { mutableStateListOf<Message>() }

                LaunchedEffect(key1 = Unit) {
                    startOtherUserMessageTimer(messages)
                }

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

    private fun startOtherUserMessageTimer(messages: MutableList<Message>) {
        val timer = Timer()
        timer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    val randomMessages = listOf("Hi!", "How are you?", "What's up?", "Cool!")
                    val randomMessage = randomMessages.random()
                    val newMessage =
                        Message("User 2", randomMessage, System.currentTimeMillis())
                    CoroutineScope(Dispatchers.Main).launch {
                        messages.add(newMessage)
                    }
                }
            },
            5000,
            5000
        )
    }

    @Composable
    fun MessageList(messages: List<Message>) {
        val groupedMessages = messages.groupBy { message ->
            val formatter = SimpleDateFormat("EEEE HH:mm", Locale.getDefault())
            formatter.format(Date(message.timestamp))
        }

        LazyColumn(reverseLayout = true) {
            groupedMessages.forEach { (section, messagesInSection) ->
                item {
                    Text(
                        text = section,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(messagesInSection) { message ->
                    MessageBubble(message)
                }
            }
        }
    }

    @Composable
    fun MessageBubble(message: Message) {
        Text(text = "${message.sender}: ${message.content}")
    }
}
