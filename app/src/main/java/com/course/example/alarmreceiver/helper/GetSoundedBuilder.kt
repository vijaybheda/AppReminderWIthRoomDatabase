package com.course.example.alarmreceiver.helper


import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.app.reminder.R
import com.course.example.alarmreceiver.model.ReminderData

object GetSoundedBuilder {
    fun getSoundedNotification(context: Context, reminderData: ReminderData): NotificationCompat.Builder {
        val soundUri: Uri = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                    context.applicationContext.packageName + "/" + R.raw.whatsapp_web_tone
        )
        return NotificationCompat.Builder(
            context.applicationContext,
            SoundNotificationHelper.channelID
        )
            .setContentTitle(reminderData.title)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentText("Notify your reminder for " + reminderData.title)
            .setSound(soundUri)
    }
}