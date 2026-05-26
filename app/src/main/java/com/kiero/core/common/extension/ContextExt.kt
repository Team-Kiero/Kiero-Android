package com.kiero.core.common.extension

import android.app.NotificationManager
import android.content.Context

fun Context.isNotificationEnabled(): Boolean {
    val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    return manager.areNotificationsEnabled()
}