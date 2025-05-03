package com.fhanafi.cerdikia.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MapelEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mapelDao(): MapelDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "user_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}