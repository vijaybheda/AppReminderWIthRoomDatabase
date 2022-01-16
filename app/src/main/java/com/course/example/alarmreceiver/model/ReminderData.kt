package com.course.example.alarmreceiver.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "reminder")
class ReminderData {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var title: String = ""
    var reminderTime: Long = System.currentTimeMillis()


    @Ignore
    constructor(name: String, email: Long) {
        this.title = name
        this.reminderTime = email
    }

    constructor(
        id: Int,
        name: String,
        email: Long
    ) {
        this.id = id
        this.title = name
        this.reminderTime = email
    }

    constructor()

    override fun toString(): String {
        return "ReminderData(id=$id, title='$title', reminderTime=$reminderTime)"
    }


}