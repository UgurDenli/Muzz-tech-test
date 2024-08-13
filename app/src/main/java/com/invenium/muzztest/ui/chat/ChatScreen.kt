package com.invenium.muzztest.ui.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.* // ktlint-disable no-wildcard-imports
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.* // ktlint-disable no-wildcard-imports
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.* // ktlint-disable no-wildcard-imports
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.invenium.muzztest.R
import com.invenium.muzztest.data.local.entity.Message
import com.invenium.muzztest.ui.chat.components.MessageBubble
import com.invenium.muzztest.ui.chat.components.TextEntryBox
import com.invenium.muzztest.ui.theme.MuzzTestTheme
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatScreen(viewModel: ChatViewModel = viewModel()) {
    MuzzTestTheme {
        val messages by viewModel.messages.observeAsState(initial = emptyList())

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { TopAppBarContent() },
                    backgroundColor = MaterialTheme.colors.primary
                )
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .background(Color.White)
                ) {
                    MessageList(
                        messages = messages,
                        isOtherUserTyping = viewModel.isOtherUserTyping,
                        modifier = Modifier.weight(1f)
                    )
                    TextEntryBox(
                        messages = messages.toMutableList(),
                        onMessageSent = { messageText ->
                            viewModel.sendMessage(messageText)
                        }
                    )
                }
            }
        )

        LaunchedEffect(key1 = viewModel.showOtherUserMessage) {
            viewModel.sendOtherUserMessage()
        }
    }
}

@Composable
fun TopAppBarContent() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = { /* Handle back button click */ }) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.mipmap.ic_muzz_foreground),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray, CircleShape)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text("Sarah", fontWeight = FontWeight.Bold)
        }
    }
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
            var showTimestamp = true
            var previousTimestamp: Long? = null
            messagesInSection.forEachIndexed { index, message ->
                showTimestamp = !(
                    previousTimestamp != null &&
                        message.timestamp - previousTimestamp!! < 300000
                    )

                if (showTimestamp) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val date = section.substringBefore(' ')
                            val time = section.substringAfter(' ')
                            Text(
                                text = date,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray,
                                fontSize = MaterialTheme.typography.body1.fontSize,
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .wrapContentSize(Alignment.Center)
                            )
                            Text(
                                text = time,
                                color = Color.Gray,
                                fontSize = MaterialTheme.typography.body2.fontSize,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }

                item {
                    MessageBubble(message)
                }

                previousTimestamp = message.timestamp
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
