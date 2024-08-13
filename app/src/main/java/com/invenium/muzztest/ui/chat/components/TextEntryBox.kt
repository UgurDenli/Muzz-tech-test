package com.invenium.muzztest.ui.chat.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.invenium.muzztest.data.local.entity.Message
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
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = messageText,
            onValueChange = { messageText = it },
            modifier = Modifier
                .weight(1f)
                .padding(bottom = bottomPadding.value),
            placeholder = { Text("Enter message") },
            enabled = !isSending
        )

        AnimatedVisibility(
            visible = !isSending,
            enter = slideInHorizontally(initialOffsetX = { it }),
            exit = slideOutHorizontally(targetOffsetX = { it })
        ) {
            Button(onClick = {
                isSending = true
                onMessageSent(messageText)
                messageText = ""
            }) {
                Text("Send")
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
