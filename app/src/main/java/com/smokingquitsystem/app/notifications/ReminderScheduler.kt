package com.smokingquitsystem.app.notifications

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.smokingquitsystem.app.MainActivity
import com.smokingquitsystem.app.R
import com.smokingquitsystem.app.data.ReminderPrefsSync
import java.util.Calendar

object ReminderScheduler {
    const val CHANNEL_ID = "quit_reminders"
    private val HOURS = intArrayOf(8, 11, 14, 17, 20)
    private const val REQUEST_CODE_BASE = 5100

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Quit reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Daily encouragement to stay smoke-free"
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    fun scheduleReminders(context: Context) {
        if (!ReminderPrefsSync.isRemindersEnabled(context)) return
        createNotificationChannel(context)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        HOURS.forEachIndexed { index, hour ->
            val intent = Intent(context, ReminderReceiver::class.java).putExtra("slot", index)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                REQUEST_CODE_BASE + index,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                if (timeInMillis <= System.currentTimeMillis()) {
                    add(Calendar.DAY_OF_YEAR, 1)
                }
            }
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }

    fun cancelReminders(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        repeat(HOURS.size) { index ->
            val intent = Intent(context, ReminderReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                REQUEST_CODE_BASE + index,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
        }
    }
}

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        ReminderScheduler.createNotificationChannel(context)
        val slot = intent.getIntExtra("slot", 0)
        val useRoman = ReminderPrefsSync.getLanguage(context) != "English"

        val messages = if (useRoman) {
            listOf(
                "Aaj ka din smoke-free rakhein. Aap kar sakte hain! 💚",
                "Craving temporary hai. Gehri saans lein aur 5 minute rukain.",
                "Har cigarette se door rehna aapki jeet hai.",
                "Apni wajah yaad karein. Aapki sehat qeemti hai.",
                "Aaj ki progress par fakhar karein. Kal se behtar aaj banayein!"
            )
        } else {
            listOf(
                "Keep today smoke-free. You've got this! 💚",
                "A craving is temporary. Breathe deeply and wait five minutes.",
                "Every cigarette avoided is a win.",
                "Remember your reason. Your health matters.",
                "Be proud of today's progress. Keep going!"
            )
        }

        val launchIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            0,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, ReminderScheduler.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_notification)
            .setContentTitle("Smoking Quit System")
            .setContentText(messages[slot.coerceIn(0, 4)])
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)
            .build()

        val canNotify = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

        if (canNotify) {
            NotificationManagerCompat.from(context).notify(700 + slot, notification)
        }
    }
}

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == Intent.ACTION_BOOT_COMPLETED || action == Intent.ACTION_MY_PACKAGE_REPLACED) {
            ReminderScheduler.scheduleReminders(context)
        }
    }
}
