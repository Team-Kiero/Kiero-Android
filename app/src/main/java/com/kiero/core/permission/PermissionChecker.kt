package com.kiero.core.permission

import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.kiero.core.permission.model.PermissionType

object PermissionChecker {
    fun isGranted(context: Context, type: PermissionType): Boolean {
        if (type == PermissionType.POST_NOTIFICATIONS) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            return manager.areNotificationsEnabled()
        }

        val permission = type.manifestPermission ?: return true
        return ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED
    }
}
