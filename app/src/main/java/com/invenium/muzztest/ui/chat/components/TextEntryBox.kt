package com.invenium.muzztest.ui.chat.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.invenium.muzztest.data.local.entity.Message
import com.invenium.muzztest.ui.theme.Pink300
import kotlinx.coroutines.delay

@Composable
fun TextEntryBox(messages: MutableList<Message>, onMessageSent: (String) -> Unit) {
    var messageText by remember { mutableStateOf("") }
    var isSending by remember { mutableStateOf(false) }

    val view = LocalView.current
    val density = LocalDensity.current

    val bottomPadding = remember {
        derivedStateOf {
            val insets = ViewCompat.getRootWindowInsets(view)?.getInsets(WindowInsetsCompat.Type.ime())
            insets?.bottom?.let { density.run { it.toDp() } } ?: 0.dp
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = messageText,
            onValueChange = { messageText = it },
            modifier = Modifier
                .weight(1f)
                .border(1.dp, Pink300, CircleShape)
                .padding(bottom = bottomPadding.value),
            placeholder = {
                Text(
                    text = "Enter message",
                    color = Color.Black
                )
            },
            enabled = !isSending,
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.width(8.dp))
        AnimatedVisibility(
            visible = !isSending,
            enter = slideInHorizontally(initialOffsetX = { it }),
            exit = slideOutHorizontally(targetOffsetX = { it })
        ) {
            IconButton(onClick = {
                isSending = true
                onMessageSent(messageText)
                messageText = ""
            }) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Pink300
                )
            }
        }

        AnimatedVisibility(
            visible = isSending,
            enter = slideInHorizontally(initialOffsetX = { -it }),
            exit = slideOutHorizontally(targetOffsetX = { -it })
        ) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp))
        }

        LaunchedEffect(key1 = isSending) {
            if (isSending) {
                delay(500)
                isSending = false
            }
        }
    }
}
