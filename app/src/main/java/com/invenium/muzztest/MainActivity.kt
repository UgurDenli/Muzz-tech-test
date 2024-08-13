package com.invenium.muzztest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.* // Only necessary imports
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.* // Only necessary imports
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.invenium.muzztest.data.local.entity.Message
import com.invenium.muzztest.ui.chat.components.MessageBubble
import com.invenium.muzztest.ui.chat.components.TextEntryBox
import com.invenium.muzztest.ui.theme.MuzzTestTheme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MuzzTestTheme {
                val messages = remember { mutableStateListOf<Message>().apply { addAll(sampleMessages) } }
                var isOtherUserTyping by remember { mutableStateOf(false) }

                Scaffold(
                    topBar = { TopAppBar(title = { Text("Muzz Chat") }) },
                    content = { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .background(Color.Black)
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                MessageList(messages = messages, isOtherUserTyping = isOtherUserTyping, modifier = Modifier.weight(1f))
                                TextEntryBox(
                                    messages = messages,
                                    onMessageSent = { messageText ->
                                        messages.add(Message("User 1", messageText, System.currentTimeMillis()))
                                    }
                                )
                            }
                        }
                    }
                )

                LaunchedEffect(key1 = Unit) {
                    delay(3000)
                    isOtherUserTyping = false
                }
            }
        }
    }

    companion object {
        val sampleMessages = listOf(
            Message("User 1", "Hello!", System.currentTimeMillis() - 10000),
            Message("User 2", "Hi there!", System.currentTimeMillis() - 8000),
            Message("User 1", "How are you?", System.currentTimeMillis() - 6000),
            Message("User 2", "I'm doing well, thanks!", System.currentTimeMillis() - 4000),
            Message("User 2", "What's up?", System.currentTimeMillis() - 2000),
            Message("User 1", "Not much, just chilling!", System.currentTimeMillis() - 1000)
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
