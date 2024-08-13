package com.invenium.muzztest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.invenium.muzztest.data.local.entity.Message
import com.invenium.muzztest.ui.chat.components.MessageBubble
import com.invenium.muzztest.ui.chat.components.TextEntryBox
import com.invenium.muzztest.ui.theme.MuzzTestTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MuzzTestTheme {
                val messages = remember { mutableStateListOf<Message>().apply { addAll(sampleMessages) } }
                var isOtherUserTyping by remember { mutableStateOf(false) }
                var showOtherUserMessage by remember { mutableStateOf(false) }
                var lastUserMessage by remember { mutableStateOf("") }

                val coroutineScope = rememberCoroutineScope()

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { TopAppBarContent() },
                            backgroundColor = MaterialTheme.colors.primary
                        )
                    },
                    content = { innerPadding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            MessageList(
                                messages = messages,
                                isOtherUserTyping = isOtherUserTyping,
                                modifier = Modifier.weight(1f)
                            )
                            TextEntryBox(
                                messages = messages,
                                onMessageSent = { messageText ->
                                    messages.add(Message("User 1", messageText, System.currentTimeMillis()))
                                    lastUserMessage = messageText
                                    coroutineScope.launch {
                                        delay(1000)
                                        showOtherUserMessage = true
                                    }
                                }
                            )
                        }
                    }
                )

                LaunchedEffect(key1 = Unit) {
                    delay(5000)
                    isOtherUserTyping = true
                    delay(3000)
                    isOtherUserTyping = false
                }

                LaunchedEffect(key1 = showOtherUserMessage) {
                    if (showOtherUserMessage) {
                        delay(2000)
                        val response = when (lastUserMessage.lowercase()) {
                            "hello" -> "Hi there!"
                            "how are you" -> "I'm doing great, thanks!"
                            "im great aswell how is your day going" -> "My day is going well, thanks for asking!"
                            else -> "Nice to hear from you!"
                        }
                        messages.add(Message("User 2", response, System.currentTimeMillis()))
                        showOtherUserMessage = false
                    }
                }
            }
        }
    }

    companion object {
        val sampleMessages = listOf(
            Message("User 1", "Hello!", System.currentTimeMillis() - 10000),
            Message("User 2", "Hi there!", System.currentTimeMillis() - 8000)
        )
    }

    @Composable
    fun MessageList(messages: List<Message>, isOtherUserTyping: Boolean, modifier: Modifier = Modifier) {
        val groupedMessages = messages.groupBy { message ->
            val formatter = SimpleDateFormat("EEEE HH:mm", Locale.getDefault())
            formatter.format(Date(message.timestamp))
        }

        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            groupedMessages.forEach { (section, messagesInSection) ->
                item {
                    Text(
                        text = section,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                var previousMessage: Message? = null
                items(messagesInSection) { message ->
                    if (previousMessage != null &&
                        message.sender == previousMessage!!.sender &&
                        message.timestamp - previousMessage!!.timestamp < 20000
                    ) {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    MessageBubble(message)
                    previousMessage = message
                }
            }
        }

        if (isOtherUserTyping) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Gray, strokeWidth = 2.dp)
            }
        }
    }
}

@Composable
fun TopAppBarContent() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = { }) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .border(1.dp, Color.Gray, CircleShape)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text("Sarah", fontWeight = FontWeight.Bold)
        }
    }
}
