package com.kiero.core.permission.ui

import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.kiero.core.permission.PermissionStatus
import com.kiero.core.permission.model.PermissionType
import timber.log.Timber

@Composable
fun rememberPermissionRequester(
    type: PermissionType,
    deniedCount: Int,
    onGranted: () -> Unit,
    onDenied: () -> Unit, // 요청 권한 다이얼로그에서 거부하였을 때 - 스낵바 등
    onPermanentlyDenied: () -> Unit,   // 설정 화면 안내 UI 띄우기용
    onCountIncrease: (PermissionType) -> Unit,
): () -> Unit {
    val activity = LocalActivity.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (isGranted) {
            onGranted()
            return@rememberLauncherForActivityResult
        }

        onCountIncrease(type)

        // 혹시나 activity가 없으면 rationale 재판정이 불가능하기 때문에 보수적으로 일반적 거부 처리
        val act = activity ?: run {
            onDenied()
            return@rememberLauncherForActivityResult
        }

        // 거부 직후 재판정으로 이번 거부로 영구 거부가 됐는지 확인
        val status = PermissionStatusResolver.resolve(act, type, deniedCount + 1)
        if (status is PermissionStatus.PermanentlyDenied) {
            onPermanentlyDenied()
        } else {
            onDenied()
        }
    }

    return request@{
        val act = activity ?: run {
            Timber.e("Activity context not found; permission request skipped for $type")
            return@request
        }

        val permission = type.manifestPermission ?: run {
            onGranted()
            return@request
        }

        when (PermissionStatusResolver.resolve(act, type, deniedCount)) {
            is PermissionStatus.Granted -> onGranted()
            is PermissionStatus.ShouldRequest -> launcher.launch(permission)
            is PermissionStatus.PermanentlyDenied -> onPermanentlyDenied()
        }
    }
}
