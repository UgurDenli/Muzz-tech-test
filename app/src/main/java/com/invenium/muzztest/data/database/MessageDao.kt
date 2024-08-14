package com.invenium.muzztest.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.invenium.muzztest.data.local.entity.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert
    fun insertMessage(message: Message): Long

    // Retrieves all messages from the database in ascending order of timestamp
    @Query("SELECT * FROM messages ORDER BY timestamp ASC")
    fun getMessages(): Flow<List<Message>>
}
