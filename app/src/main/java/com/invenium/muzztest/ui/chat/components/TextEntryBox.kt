package com.invenium.muzztest.ui.chat.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.* // ktlint-disable no-wildcard-imports
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.invenium.muzztest.data.local.entity.Message

@Composable
fun TextEntryBox(messages: MutableList<Message>, onMessageSent: (String) -> Unit) {
    var messageText by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            value = messageText,
            onValueChange = { messageText = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Enter message") }
        )
        Button(onClick = {
            onMessageSent(messageText)
            messageText = ""
        }) {
            Text("Send")
        }
    }
}