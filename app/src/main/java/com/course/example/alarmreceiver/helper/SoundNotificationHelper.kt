package com.course.example.alarmreceiver.helper

import android.app.NotificationManager

import android.content.ContentResolver

import android.media.AudioAttributes

import android.app.NotificationChannel
import android.content.Context

import android.os.Build

import androidx.annotation.RequiresApi

import android.content.ContextWrapper
import android.net.Uri
import com.app.reminder.R


class SoundNotificationHelper(context: Context) : ContextWrapper(context) {
    private var mManager: NotificationManager? = null

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)

        // For API 26+, the sound should be added here also
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .build()
//        channel.setSound(
//            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.packageName + "/" + R.raw.whatsapp_web_tone),
//            audioAttributes
//        )
        manager!!.createNotificationChannel(channel)
    }

    val manager: NotificationManager?
        get() {
            if (mManager == null) {
                mManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            return mManager
        }

    companion object {
        const val channelID = "soundChannel"
        const val channelName = "Sound Notification"
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }
}
