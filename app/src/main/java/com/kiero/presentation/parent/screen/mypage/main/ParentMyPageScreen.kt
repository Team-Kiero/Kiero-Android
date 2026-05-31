package com.kiero.presentation.parent.screen.mypage.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.BuildConfig
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.component.dialog.KieroDialog
import com.kiero.core.designsystem.component.dialog.action.KieroCancelAction
import com.kiero.core.designsystem.component.dialog.action.KieroConfirmAction
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.parent.ParentInfo
import com.kiero.core.permission.PermissionChecker
import com.kiero.core.permission.model.PermissionType
import com.kiero.core.permission.util.navigateToSettings
import com.kiero.presentation.parent.screen.mypage.main.component.AlarmSettingItem
import com.kiero.presentation.parent.screen.mypage.main.component.ParentMyPageUserInfo
import com.kiero.presentation.parent.screen.mypage.main.component.SettingItem
import com.kiero.presentation.parent.screen.mypage.main.model.ParentMenuLinkType
import com.kiero.presentation.parent.screen.mypage.main.model.actions.ParentMyPageActions

@Composable
fun ParentMyPageRoute(
    paddingValues: PaddingValues,
    navigateToOssLicenses: () -> Unit,
    navigateToParentChildCare: () -> Unit,
    navigateToWithdraw: () -> Unit,
    viewModel: ParentMyPageViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var isLogOut by remember { mutableStateOf(false) }

    var showSettingsDialog by remember { mutableStateOf(false) }
    var isWaitingForSettingsResult by remember { mutableStateOf(false) }

    val context = LocalContext.current

   LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        val hasOsPermission = PermissionChecker.isGranted(
            context = context,
            type = PermissionType.POST_NOTIFICATIONS
        )

        if (isWaitingForSettingsResult) {
            if (hasOsPermission) {
                viewModel.updateIsAlarmChecked(true)
            } else {
                viewModel.updateIsAlarmChecked(false)
            }
            isWaitingForSettingsResult = false
        } else {
            if (!hasOsPermission && state.isAlarmChecked) {
                viewModel.updateIsAlarmChecked(false)
            }
        }
    val uriHandler = LocalUriHandler.current
    val globalTrigger = LocalGlobalUiEventTrigger.current

    var isLogOut by remember {
        mutableStateOf(false)
    }

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is ParentMyPageSideEffect.ShowToast -> globalTrigger.showToast(it.message)
            is ParentMyPageSideEffect.ShowSnackBar -> globalTrigger.showSnackbar(
                SnackbarState(
                    message = it.message,
                    bottomPadding = 110
                )
            )

            is ParentMyPageSideEffect.NavigateToChildCare -> navigateToParentChildCare()
            is ParentMyPageSideEffect.NavigateToWithdraw -> navigateToWithdraw()
        }
    }

    val actions = remember(viewModel) {
        object : ParentMyPageActions {
            override fun onClickChildCare() = navigateToParentChildCare()

            override fun onClickLogOut() {
                isLogOut = true
            }

            override fun onClickLogOutConfirm() {
                isLogOut = false
                viewModel.logOut()
            }

            override fun onClickLogOutCancel() {
                isLogOut = false
            }

            override fun onClickWithdraw() = navigateToWithdraw()

            override fun onClickOss() = navigateToOssLicenses()

            override fun onCheckedChange(checked: Boolean) {
                viewModel.updateIsAlarmChecked(checked)
            }

            override fun onClickTerms(type: ParentMenuLinkType) {
                val link = state.myPageMenus.find { it.linkType == type }?.link

                if (!link.isNullOrEmpty()) {
                    uriHandler.openUri(link)
                } else {
                    globalTrigger.showToast("링크를 찾을 수 없습니다.")
                }
            }
        }
    }

    ParentMyPageScreen(
        paddingValues = paddingValues,
        state = state,
        isLogOut = isLogOut,
        onClickChildCare = navigateToParentChildCare,
        onClickLogOut = { isLogOut = true },
        onClickLogOutConfirm = {
            isLogOut = false
            viewModel.logOut()
        },
        onClickLogOutCancel = { isLogOut = false },
        onClickWithDraw = navigateToWithDraw,
        onClickOss = navigateToOssLicenses,
        onCheckedChange = { isChecked ->
            if (isChecked) {
                val hasOsPermission = PermissionChecker.isGranted(context, PermissionType.POST_NOTIFICATIONS)

                if (hasOsPermission) {
                    viewModel.updateIsAlarmChecked(true)
                } else {
                    showSettingsDialog = true
                }
            } else {
               viewModel.updateIsAlarmChecked(false)
            }
        }
    )

    if (showSettingsDialog) {
        KieroDialog(
            isDisabled = false,
            onDismiss = {
                showSettingsDialog = false
                viewModel.updateIsAlarmChecked(false)
            },
            title = "설정에서 알림을 켜주세요.",
            subDescription = "아이의 일정과 미션 알림을 받으려면\n설정에서 키어로 알림을 켜주세요.",
            confirmAction = KieroConfirmAction(
                text = "기기 설정으로 이동하기",
                onClick = {
                    showSettingsDialog = false
                    isWaitingForSettingsResult = true
                    context.navigateToSettings(PermissionType.POST_NOTIFICATIONS)
                }
            )
        )
    }
}

@Composable
private fun ParentMyPageScreen(
    paddingValues: PaddingValues,
    state: ParentMyPageState,
    isLogOut: Boolean,
    actions: ParentMyPageActions,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = KieroTheme.colors.black)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = KieroTheme.colors.black
                )
                .padding(paddingValues)
        ) {
            ParentMyPageUserInfo(
                info = state.parentInfo,
            )

            Spacer(modifier = Modifier.height(11.dp))

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
            ) {
                SettingItem(
                    text = "자녀 관리",
                    onClick = actions::onClickChildCare,
                    connectionStatus = state.connectionStatus,
                    showConnectionStatus = true,
                )

                AlarmSettingItem(
                    text = "푸시 알림",
                    checked = state.isAlarmChecked,
                    enabled = true,
                    onCheckedChange = actions::onCheckedChange
                )

                SettingItem(
                    text = "고객 지원",
                    onClick = {
                        actions.onClickTerms(ParentMenuLinkType.CUSTOMER_SUPPORT)
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "약관 및 정책",
                    style = KieroTheme.typography.regular.body4,
                    color = KieroTheme.colors.gray400,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                )

                Spacer(modifier = Modifier.height(13.dp))

                SettingItem(
                    text = "서비스 이용약관",
                    onClick = {
                        actions.onClickTerms(ParentMenuLinkType.SERVICE_TERMS)
                    },
                )

                SettingItem(
                    text = "개인정보 처리방침",
                    onClick = {
                        actions.onClickTerms(ParentMenuLinkType.PRIVACY_POLICY)
                    }
                )

                SettingItem(
                    text = "오픈소스 라이선스",
                    onClick = actions::onClickOss
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column (
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.spacedBy(11.dp)
            ) {
                Text(
                    text = "앱 버전 v${BuildConfig.VERSION_NAME}",
                    style = KieroTheme.typography.regular.body4,
                    color = KieroTheme.colors.gray400,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Text(
                    text = "로그아웃",
                    style = KieroTheme.typography.regular.body4,
                    color = KieroTheme.colors.gray400,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .fillMaxWidth()
                        .noRippleClickable(onClick = actions::onClickLogOut)
                )

                Text(
                    text = "회원탈퇴",
                    style = KieroTheme.typography.regular.body4,
                    color = KieroTheme.colors.gray400,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .fillMaxWidth()
                        .noRippleClickable(onClick = actions::onClickWithdraw)
                )

                Spacer(modifier = Modifier.height(70.dp))
            }
        }

        if (isLogOut) {
            KieroDialog(
                isDisabled = true,
                onDismiss = actions::onClickLogOutCancel,
                confirmAction = KieroConfirmAction(
                    onClick = actions::onClickLogOutConfirm
                ),
                cancelAction = KieroCancelAction(
                    onClick = actions::onClickLogOutCancel
                ),
                title = "로그아웃",
                subDescription = "로그아웃 하시겠습니까?"
            )
        }
    }
}

@Preview
@Composable
private fun ParentMyPageScreenPreview() {
    KieroTheme {
        ParentMyPageScreen(
            paddingValues = PaddingValues(),
            state = ParentMyPageState(
                parentInfo = ParentInfo(name = "키어로")
            ),
            isLogOut = false,
            actions = object : ParentMyPageActions {
                override fun onClickChildCare() {}
                override fun onClickLogOut() {}
                override fun onClickLogOutConfirm() {}
                override fun onClickLogOutCancel() {}
                override fun onClickWithdraw() {}
                override fun onClickOss() {}
                override fun onCheckedChange(checked: Boolean) {}
                override fun onClickTerms(type: ParentMenuLinkType) {}
            }
        )
    }
}
