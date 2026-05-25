package com.kiero.core.permission.model

import android.Manifest
import android.os.Build

enum class PermissionType(
    val manifestPermission: String?
) {
    CAMERA(Manifest.permission.CAMERA),
    POST_NOTIFICATIONS(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.POST_NOTIFICATIONS
        } else {
            null
        },
    ),
}
