package com.kiero.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.kiero.R
import com.kiero.presentation.main.activity.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KieroNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val notificationManager: NotificationManager? =
        context.getSystemService(NotificationManager::class.java)

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Kiero 알림",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Kiero 앱의 주요 알림을 받습니다."
        }
        notificationManager?.createNotificationChannel(channel)
    }

    fun showPushNotification(title: String, body: String, data: Map<String, String>) {
        val intent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            data.forEach { (key, value) ->
                putExtra(key, value)
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_push_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setColor(ContextCompat.getColor(context, R.color.kiero_mint))
            .setGroup("KIERO_PUSH_GROUP")
            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_ALL)

        val uniqueId = System.currentTimeMillis().toInt()

        try {
            notificationManager?.notify(uniqueId, notificationBuilder.build())
        } catch (e: SecurityException) {
            Timber.e(e, "권한 문제로 인해 알림을 보내는 데 실패했습니다.")
        }
    }

    companion object {
        private const val CHANNEL_ID = "kiero_push_channel"
    }
}