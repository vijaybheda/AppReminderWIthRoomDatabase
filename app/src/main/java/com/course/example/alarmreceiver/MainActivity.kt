package com.course.example.alarmreceiver

import android.os.Bundle
import android.content.Intent
import android.app.PendingIntent
import android.app.AlarmManager
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.app.reminder.R
import com.course.example.alarmreceiver.database.AppDatabase
import com.course.example.alarmreceiver.database.AppExecutors
import com.course.example.alarmreceiver.helper.ReminderReceiver
import com.course.example.alarmreceiver.model.ReminderData
import java.util.*

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.widget.TimePicker

import android.app.TimePickerDialog
import android.content.Context
import androidx.core.app.AlarmManagerCompat
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import android.os.Build.VERSION_CODES

import android.os.Build.VERSION
import android.content.BroadcastReceiver

import android.content.IntentFilter

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var mDb: AppDatabase? = null

    private lateinit var dCalendar: Calendar

    private lateinit var etNotificationTitle: TextInputEditText
    private lateinit var etNotificationDate: TextInputEditText
    private lateinit var etNotificationTime: TextInputEditText
    private lateinit var btnAddReminder: Button

    private lateinit var selectedTime: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mDb = AppDatabase.getInstance(this@MainActivity)

        selectedTime = Date()
        val time = Calendar.getInstance()
        time.add(Calendar.HOUR, 1)
        dCalendar = time

        etNotificationTitle = findViewById(R.id.etNotificationTitle)
        etNotificationDate = findViewById(R.id.etNotificationDate)
        etNotificationTime = findViewById(R.id.etNotificationTime)
        btnAddReminder = findViewById(R.id.btnAddReminder)

        btnAddReminder.setOnClickListener(this)
        etNotificationDate.setOnClickListener(this)
        etNotificationTime.setOnClickListener(this)

        updateLabel()

//        testReminder(123, System.currentTimeMillis()+5)
//        testReminder(123, 1641100870000)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnAddReminder -> {
                addReminder()
            }
            R.id.etNotificationDate -> {
                showDatePicker()
            }
            R.id.etNotificationTime -> {
                showTimePicker()
            }
        }
    }

    private fun showDatePicker() {
        DatePickerDialog(
            this@MainActivity,
            dateSetListener,
            dCalendar[Calendar.YEAR],
            dCalendar[Calendar.MONTH],
            dCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }

    private fun getBroadcastPendingIntent(
        context: Context,
        id: Int,
        intent: Intent
    ): PendingIntent? {
        var flags = PendingIntent.FLAG_UPDATE_CURRENT
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            flags = flags or PendingIntent.FLAG_IMMUTABLE
        }
        return PendingIntent.getBroadcast(context, id, intent, flags)
    }

    private fun addReminder() {

        if (mDb == null) {
            mDb = AppDatabase.getInstance(this@MainActivity);
        }

        val reminderData = ReminderData()
        reminderData.id = 0
        reminderData.reminderTime = selectedTime.time
        reminderData.title = etNotificationTitle.text.toString()
        AppExecutors.instance?.diskIO()?.execute {
            mDb?.reminderDataDao()?.insertReminder(reminderData)
        }

        val milliSeconds = selectedTime.time
        val requestCode = milliSeconds.toInt()
        val intent = Intent(this, ReminderReceiver::class.java)
        intent.putExtra("ReminderData", selectedTime.time)
        val pendingIntent = getBroadcastPendingIntent(applicationContext, requestCode, intent)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//        alarmManager[AlarmManager.RTC_WAKEUP, selectedTime.time] = pendingIntent
        intent.action = milliSeconds.toString()

        if (pendingIntent != null) {
            AlarmManagerCompat.setExact(
                alarmManager,
                AlarmManager.RTC_WAKEUP,
                milliSeconds,
                pendingIntent)
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
//        } else {
//            alarmManager.set(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
//        }

    }

    private var dateSetListener =
        OnDateSetListener { _, year, month, day ->
            dCalendar.set(Calendar.YEAR, year)
            dCalendar.set(Calendar.MONTH, month)
            dCalendar.set(Calendar.DAY_OF_MONTH, day)

            updateLabel()
        }

    private fun showTimePicker() {
        TimePickerDialog(this,
            fun(_: TimePicker, hourOfDay: Int, minute: Int) {
                dCalendar[Calendar.HOUR_OF_DAY] = hourOfDay
                dCalendar[Calendar.MINUTE] = minute

                updateLabel()
            },
            dCalendar[Calendar.HOUR_OF_DAY],
            dCalendar[Calendar.MINUTE],
            false
        ).show()
    }

    private fun updateLabel() {
        selectedTime = dCalendar.time
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("hh:mm", Locale.getDefault())

        etNotificationDate.setText(dateFormat.format(selectedTime).toString())
        etNotificationTime.setText(timeFormat.format(selectedTime).toString())
    }

    private fun testReminder(code : Int, sec : Long) {

        val requestCode = sec.toInt()
        val intent = Intent(this, ReminderReceiver::class.java)
        val pendingIntent = getBroadcastPendingIntent(applicationContext, requestCode, intent)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        intent.action = sec.toString()

        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        val mReceiver: BroadcastReceiver = ReminderReceiver()
        registerReceiver(mReceiver, filter)

        if (pendingIntent != null) {
            AlarmManagerCompat.setExact(
                alarmManager,
                AlarmManager.RTC_WAKEUP,
                sec,
                pendingIntent)
        }
        finish()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
//        } else {
//            alarmManager.set(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
//        }
//        Toast.makeText(
//            this, "Alarm set",
//            Toast.LENGTH_LONG
//        ).show()// TODO remove toast


    }
}