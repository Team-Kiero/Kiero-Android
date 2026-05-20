package com.kiero.core.permission.ui

import android.app.Activity
import com.kiero.core.permission.PermissionChecker
import com.kiero.core.permission.PermissionStatus
import com.kiero.core.permission.model.PermissionType

object PermissionStatusResolver {
    fun resolve(
        activity: Activity,
        type: PermissionType,
        deniedCount: Int,
    ): PermissionStatus {
        if (PermissionChecker.isGranted(activity, type)) {
            return PermissionStatus.Granted
        }

        val permission = type.manifestPermission
            ?: return PermissionStatus.ShouldRequest(showRationale = false)

        val shouldShowRationale =
            activity.shouldShowRequestPermissionRationale(permission)

        return when {
            shouldShowRationale -> PermissionStatus.ShouldRequest(showRationale = true)
            deniedCount == 0 -> PermissionStatus.ShouldRequest(showRationale = false)
            else -> PermissionStatus.PermanentlyDenied
        }
    }
}
