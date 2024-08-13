package com.invenium.muzztest.ui.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.invenium.muzztest.data.local.entity.Message
import com.invenium.muzztest.ui.theme.Pink300

@Composable
fun MessageBubble(message: Message) {
    val isSentByCurrentUser = message.sender == "User 1"
    val bubbleColor = if (isSentByCurrentUser) Pink300 else Color.LightGray
    val textColor = if (isSentByCurrentUser) Color.White else Color.Black

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = if (isSentByCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .background(bubbleColor, CircleShape)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message.content,
                color = textColor
            )
        }
    }
}