package com.invenium.muzztest.ui.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.invenium.muzztest.data.database.MuzzDatabase
import com.invenium.muzztest.data.local.entity.Message
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatViewModel(private val database: MuzzDatabase) : ViewModel() {
    private val messageDao = database.messageDao()
    val messages = messageDao.getMessages().asLiveData()
    var isOtherUserTyping by mutableStateOf(false)
    var showOtherUserMessage by mutableStateOf(false)

    init {
        viewModelScope.launch {
            delay(5000)
            isOtherUserTyping = true
            delay(3000)
            isOtherUserTyping = false
        }
    }

    fun sendMessage(messageText: String) {
        viewModelScope.launch {
            messageDao.insertMessage(Message(sender = "User 1", content = messageText, timestamp = System.currentTimeMillis()))
            delay(1000)
            showOtherUserMessage = true
        }
    }

    fun sendOtherUserMessage() {
        viewModelScope.launch {
            if (showOtherUserMessage) {
                delay(2000)
                val lastUserMessage = messages.value?.lastOrNull()?.content ?: ""
                val response = when (lastUserMessage.lowercase()) {
                    "hello" -> "Hi there!"
                    "how are you" -> "I'm doing great, thanks!"
                    "im great aswell how is your day going" -> "My day is going well, thanks for asking!"
                    else -> "Nice to hear from you!"
                }
                messageDao.insertMessage(Message(sender = "User 2", content = response, timestamp = System.currentTimeMillis()))
                showOtherUserMessage = false
            }
        }
    }
}