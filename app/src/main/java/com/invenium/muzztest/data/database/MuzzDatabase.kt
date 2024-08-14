package com.invenium.muzztest.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.invenium.muzztest.data.local.entity.Message

@Database(entities = [Message::class], version = 1, exportSchema = false)
abstract class MuzzDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile private var INSTANCE: MuzzDatabase? = null

        // Returns the database instance, but of course if there isnt one it will create it if it doesn't exist
        fun getInstance(context: Context): MuzzDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): MuzzDatabase {
            return Room.databaseBuilder(context, MuzzDatabase::class.java, "muzz_database")
                .allowMainThreadQueries() .build()
        }
    }
}
