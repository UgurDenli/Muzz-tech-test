package com.invenium.muzztest.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.invenium.muzztest.data.database.MuzzDatabase

class ChatViewModelFactory(private val database: MuzzDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
