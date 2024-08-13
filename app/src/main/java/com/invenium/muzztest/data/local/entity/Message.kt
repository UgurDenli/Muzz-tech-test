package com.invenium.muzztest.data.local.entity

data class Message(
    val sender: String,
    val content: String,
    val timestamp: Long //  timestamp in milliseconds
)