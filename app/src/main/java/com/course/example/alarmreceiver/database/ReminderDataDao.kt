package com.course.example.alarmreceiver.database

import androidx.room.*
import com.course.example.alarmreceiver.model.ReminderData

@Dao
interface ReminderDataDao {
    @Query("SELECT * FROM REMINDER ORDER BY ID")
    fun loadAllReminders(): List<ReminderData?>?

    @Query("Delete FROM REMINDER ")
    fun deleteAll()

    @Insert
    fun insertReminder(reminderData: ReminderData?)

    @Update
    fun updateReminder(reminderData: ReminderData?)

    @Delete
    fun delete(reminderData: ReminderData?)

    @Query("SELECT * FROM reminder WHERE id = :id")
    fun loadReminderById(id: Int): ReminderData?
}