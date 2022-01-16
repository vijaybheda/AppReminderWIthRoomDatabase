package com.course.example.alarmreceiver.database

import androidx.room.Room

import androidx.room.RoomDatabase

import androidx.room.Database
import android.content.Context
import android.util.Log
import com.course.example.alarmreceiver.model.ReminderData

@Database(entities = [ReminderData::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDataDao(): ReminderDataDao?

    companion object {
        private val LOG_TAG = AppDatabase::class.java.simpleName
        private val LOCK = Any()
        private const val DATABASE_NAME = "remindersdata"
        private var sInstance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase? {
            if (sInstance == null) {
                synchronized(LOCK) {
                    Log.d(LOG_TAG, "Creating new database instance")
                    sInstance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, DATABASE_NAME
                    )
                        .build()
                }
            }
            Log.d(LOG_TAG, "Getting the database instance")
            return sInstance
        }
    }
}