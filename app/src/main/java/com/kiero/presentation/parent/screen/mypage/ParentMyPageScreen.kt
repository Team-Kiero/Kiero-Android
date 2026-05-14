package com.kiero.presentation.parent.screen.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.designsystem.component.dialog.KieroDialog
import com.kiero.core.designsystem.component.dialog.action.KieroCancelAction
import com.kiero.core.designsystem.component.dialog.action.KieroConfirmAction
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.parent.ParentInfo
import com.kiero.core.model.trigger.SnackbarState
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.presentation.parent.screen.mypage.component.AlarmSettingItem
import com.kiero.presentation.parent.screen.mypage.component.ParentMyPageUserInfo
import com.kiero.presentation.parent.screen.mypage.component.SettingItem

@Composable
fun ParentMyPageRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToOssLicenses: () -> Unit,
    viewModel: ParentMyPageViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var isLogOut by remember {
        mutableStateOf(false)
    }
    
    val globalTrigger = LocalGlobalUiEventTrigger.current

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is ParentMyPageSideEffect.ShowToast -> globalTrigger.showToast(it.message)
            is ParentMyPageSideEffect.ShowSnackBar -> globalTrigger.showSnackbar(
                SnackbarState(
                    message = it.message,
                )
            )
        }
    }

    ParentMyPageScreen(
        paddingValues = paddingValues,
        state = state,
        isLogOut = isLogOut,
        navigateUp = navigateUp,
        onClickChildCare = {}, // Todo: 2스에 구현될 예정,
        onClickLogOut = {
            isLogOut = true
        },
        onClickLogOutConfirm = {
            isLogOut = false
            viewModel.logOut()
        },
        onClickLogOutCancel = {
            isLogOut = false
        },
        onClickOss = navigateToOssLicenses,
        onCheckedChange = {
            viewModel.updateIsAlarmChecked(it)
        }
    )
}

@Composable
private fun ParentMyPageScreen(
    paddingValues: PaddingValues,
    state: ParentMyPageState,
    isLogOut: Boolean,
    navigateUp: () -> Unit,
    onClickChildCare: () -> Unit = {},
    onClickLogOut: () -> Unit = {},
    onClickLogOutConfirm: () -> Unit = {},
    onClickLogOutCancel: () -> Unit = {},
    onClickOss: () -> Unit = {},
    onCheckedChange: (Boolean) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = KieroTheme.colors.black)
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
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                SettingItem(
                    text = "자녀 관리",
                    onClick = onClickChildCare,
                    connectChildren = state.connectedChildren,
                    hasConnectChildren = true
                )

                AlarmSettingItem(
                    text = "푸시 알림",
                    checked = state.isAlarmChecked,
                    enabled = true,
                    onCheckedChange = onCheckedChange
                )

                SettingItem(
                    text = "로그아웃",
                    onClick = onClickLogOut
                )

                SettingItem(
                    text = "오픈소스 라이선스",
                    onClick = onClickOss
                )
            }
        }

        if (isLogOut) {
            KieroDialog(
                isDisabled = true,
                onDismiss = onClickLogOutCancel,
                confirmAction = KieroConfirmAction(
                    onClick = onClickLogOutConfirm
                ),
                cancelAction = KieroCancelAction(
                    onClick = onClickLogOutCancel
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
            navigateUp = {},
            state = ParentMyPageState(
                parentInfo = ParentInfo(name = "키어로")
            ),
            isLogOut = false
        )
    }
}
