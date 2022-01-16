package com.course.example.alarmreceiver.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.os.Vibrator
import android.util.Log
import com.course.example.alarmreceiver.MainActivity
import com.course.example.alarmreceiver.model.ReminderData
import java.text.SimpleDateFormat
import java.util.*

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("MyBroadcastReceiver", "onReceive: " + SimpleDateFormat("hh:mm:ss a").format(Date()))

        val sNH = SoundNotificationHelper(context)
        val nb = GetSoundedBuilder.getSoundedNotification(context, ReminderData())
        sNH.manager!!.notify(Constants.GET_SOUNDED_NOTIFICATION, nb.build())

        val action = intent.action
        if (action != null && Intent.ACTION_SCREEN_ON == action) {
            Log.e("BAM", "ACTION_SCREEN_ON")
            val i = Intent(context, MainActivity::class.java)
            i.putExtra("Reminder", "ABC")
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(i)
        }
    }
}