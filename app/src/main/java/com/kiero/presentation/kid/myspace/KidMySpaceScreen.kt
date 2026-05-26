package com.kiero.presentation.kid.myspace

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.BuildConfig
import com.kiero.core.designsystem.component.dialog.KieroDialog
import com.kiero.core.designsystem.component.dialog.action.KieroCancelAction
import com.kiero.core.designsystem.component.dialog.action.KieroConfirmAction
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.permission.PermissionChecker
import com.kiero.core.permission.model.PermissionType
import com.kiero.core.permission.ui.rememberPermissionRequester
import com.kiero.core.permission.util.navigateToSettings
import com.kiero.presentation.kid.component.KidProfileChip
import com.kiero.presentation.kid.myspace.component.KidMySpaceNotification
import com.kiero.presentation.kid.myspace.component.KidMySpaceSettingItem
import com.kiero.presentation.kid.myspace.component.KidMySpaceWishArchive
import com.kiero.presentation.kid.myspace.state.KidMySpaceState

@Composable
fun KidMySpaceRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToWishArchive: () -> Unit,
    navigateToPolicy: () -> Unit,
    viewModel: KidMySpaceViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        val isEnabled = PermissionChecker.isGranted(context, PermissionType.POST_NOTIFICATIONS)
        viewModel.onNotificationToggle(isEnabled)
    }

    val notificationPermissionRequester = rememberPermissionRequester(
        type = PermissionType.POST_NOTIFICATIONS,
        deniedCount = 0,
        onGranted = {
            viewModel.onNotificationToggle(true)
        },
        onDenied = {
            viewModel.onNotificationToggle(false)
        },
        onPermanentlyDenied = {
            // Android 12 이하이거나 영구 거부 → 설정 안내 다이얼로그 노출
            viewModel.showNotificationDialog(true)
        },
        onCountIncrease = {}
    )

    KidMySpaceScreen(
        paddingValues = paddingValues,
        state = state,
        onClickWishArchive = navigateToWishArchive,
        navigateToPolicy = navigateToPolicy,
        onNotificationToggle = { isChecked ->
            if (isChecked) {
                notificationPermissionRequester()
            } else {
                viewModel.onNotificationToggle(false)
            }
        },
        onNotificationDialogDismiss = viewModel::onNotificationDialogDismiss,
        onNavigateToNotificationSettings = {
            viewModel.onNotificationDialogDismiss()
            context.navigateToSettings(PermissionType.POST_NOTIFICATIONS)
        }
    )
}

@Composable
private fun KidMySpaceScreen(
    paddingValues: PaddingValues,
    state: KidMySpaceState,
    onClickWishArchive: () -> Unit,
    navigateToPolicy: () -> Unit,
    onNotificationToggle: (Boolean) -> Unit,
    onNotificationDialogDismiss: () -> Unit,
    onNavigateToNotificationSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = KieroTheme.colors.black)
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {
        KidProfileChip(
            kidName = state.kidName,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        KidMySpaceWishArchive(onClick = onClickWishArchive)

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalDivider(
            thickness = 2.dp,
            color = KieroTheme.colors.gray900,
        )

        Spacer(modifier = Modifier.height(17.dp))

        KidMySpaceNotification(
            isChecked = state.isNotificationChecked,
            onCheckedChange = onNotificationToggle
        )

        Spacer(modifier = Modifier.height(17.dp))

        KidMySpaceSettingItem(
            text = "키어로 이용 약속",
            onClick = navigateToPolicy,
        )

        KidMySpaceSettingItem(
            text = "키어로 나가기",
            onClick = { showLogoutDialog = true }
        )

        Text(
            text = "앱 버전 v${BuildConfig.VERSION_NAME}",
            style = KieroTheme.typography.regular.body4,
            color = KieroTheme.colors.gray500,
            modifier = Modifier.padding(top = 17.dp, start = 9.dp)
        )
    }

    // 알림 설정 안내 팝업 (영구 거부 또는 Android 12 이하)
    if (state.showNotificationDialog) {
        KieroDialog(
            onDismiss = onNotificationDialogDismiss,
            title = "설정에서 알림을 켜줘!",
            subDescription = "알림을 받으려면 설정에서 키어로 알림을 허용해줘!",
            cancelAction = KieroCancelAction(
                text = "취소",
                onClick = onNotificationDialogDismiss
            ),
            confirmAction = KieroConfirmAction(
                text = "설정으로 이동",
                onClick = onNavigateToNotificationSettings
            ),
            content = {}
        )
    }

    if (showLogoutDialog) {
        KieroDialog(
            onDismiss = { showLogoutDialog = false },
            title = "키어로에서 나갈거야?",
            subDescription = "다시 들어오려면 초대코드가 필요해!",
            cancelAction = KieroCancelAction(
                text = "취소",
                onClick = { showLogoutDialog = false }
            ),
            confirmAction = KieroConfirmAction(
                text = "나가기",
                onClick = { showLogoutDialog = false }
            ),
            content = {}
        )
    }
}

@Preview
@Composable
private fun KidMySpaceScreenPreview() {
    KieroTheme {
        KidMySpaceScreen(
            paddingValues = PaddingValues(),
            state = KidMySpaceState(),
            onClickWishArchive = {},
            navigateToPolicy = {},
            onNotificationToggle = {},
            onNotificationDialogDismiss = {},
            onNavigateToNotificationSettings = {}
        )
    }
}