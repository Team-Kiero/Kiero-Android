package com.kiero.core.permission.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.kiero.core.permission.model.PermissionType

/**
 * 권한 타입에 맞는 설정 화면 Intent를 생성하여 반환
 */
private fun Context.getSettingsIntent(type: PermissionType): Intent {
    val packageUri = Uri.fromParts("package", packageName, null)

    return when (type) {
        // 일반 런타임 권한 (카메라, 위치 등) -> 앱 상세 설정
        PermissionType.CAMERA -> {
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = packageUri
            }
        }

        // 알림 권한 -> 알림 전용 설정
        PermissionType.POST_NOTIFICATIONS -> {
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            }
        }
    }.apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}

/**
 * Intent 반환 없이 바로 설정 화면으로 이동하고 싶을 때 사용하는 편의성 함수
 */
fun Context.navigateToSettings(type: PermissionType) {
    startActivity(getSettingsIntent(type))
}
