package com.kiero.presentation.signup.parent

import android.content.ClipData
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.core.common.extension.collectSingleEvent
import com.kiero.core.designsystem.component.dialog.KieroDialog
import com.kiero.core.designsystem.component.dialog.action.KieroCancelAction
import com.kiero.core.designsystem.component.dialog.action.KieroConfirmAction
import com.kiero.core.designsystem.component.indicator.KieroLoadingIndicator
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.trigger.SnackbarState
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.presentation.signup.parent.component.ParentSignUpTopBar
import com.kiero.presentation.signup.parent.model.ParentSignUpStep
import com.kiero.presentation.signup.parent.screen.ParentSignUpAddChildScreen
import com.kiero.presentation.signup.parent.screen.ParentSignUpInviteScreen
import com.kiero.presentation.signup.parent.state.ParentSignUpSideEffect
import com.kiero.presentation.signup.parent.state.ParentSignUpState
import com.kiero.presentation.signup.parent.viewmodel.ParentSignUpViewModel


@Composable
fun ParentSignUpRoute(
    paddingValues: PaddingValues,
    navigateToParent: () -> Unit,
    navigateToSelection: () -> Unit,
    viewModel: ParentSignUpViewModel = hiltViewModel()
) {
    val clipboardManager = LocalClipboard.current
    val globalTrigger = LocalGlobalUiEventTrigger.current
    val focusManager = LocalFocusManager.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.sideEffect.collectSingleEvent {
        when (it) {
            ParentSignUpSideEffect.NavigateToParent -> navigateToParent()

            ParentSignUpSideEffect.NavigateToSelection -> navigateToSelection()

            is ParentSignUpSideEffect.CopyText -> {
                clipboardManager.setClipEntry(
                    ClipEntry(
                        ClipData.newPlainText(
                            it.targetText,
                            it.targetText
                        )
                    )
                )

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                    globalTrigger.showSnackbar(
                        SnackbarState(
                            message = it.message,
                            bottomPadding = 110
                        )
                    )
                }
            }

            is ParentSignUpSideEffect.ShowSnackbar -> {
                globalTrigger.showSnackbar(
                    SnackbarState(
                        message = it.message
                    )
                )
            }
            is ParentSignUpSideEffect.OnChildJoined -> {
                globalTrigger.showSnackbar(
                    SnackbarState(
                        message = "자녀 연동이 완료되었습니다!",
                        bottomPadding = 100
                    )
                )
            }
        }
    }

    BackHandler(
        enabled = state.currentStep == ParentSignUpStep.INVITE,
        onBack = viewModel::onBackClick
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ParentSignInEntryScreen(
            paddingValues = paddingValues,
            state = state,
            onLogOut = viewModel::onProfileClick
        ) {
            when (state.currentStep) {
                ParentSignUpStep.ADDCHILD -> {
                    ParentSignUpAddChildScreen(
                        state = state,
                        onNextClick = viewModel::onNextClick,
                        nextFocus = {
                            focusManager.moveFocus(FocusDirection.Down)
                        },
                        doneFocus = {
                            focusManager.clearFocus()
                        }
                    )
                }

                ParentSignUpStep.INVITE -> {
                    ParentSignUpInviteScreen(
                        state = state,
                        onStartClick = viewModel::onNextClick,
                        onCopyClick = viewModel::onCopyClick,
                        onReIssueClick = viewModel::postChild
                    )
                }
            }
        }

        if (state.isLogoutDialogVisible) {
            KieroDialog(
                title = "로그아웃",
                subDescription = "로그아웃 하시겠습니까?",
                onDismiss = viewModel::onLogoutCancel,
                confirmAction = KieroConfirmAction(
                    text = "확인",
                    onClick = {
                        viewModel.onLogoutConfirm()
                    }
                ),
                cancelAction = KieroCancelAction(
                    text = "취소",
                    onClick = viewModel::onLogoutCancel
                ),
                isDisabled = true,
                content = {}
            )
        }

        if (state.isLoading) {
            KieroLoadingIndicator()
        }
    }
}

@Composable
private fun ParentSignInEntryScreen(
    paddingValues: PaddingValues,
    state: ParentSignUpState,
    onLogOut: () -> Unit,
    content: @Composable () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = KieroTheme.colors.black
            )
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
            .padding(paddingValues)
            .padding(horizontal = 16.dp, vertical = 25.dp)
    ) {
        ParentSignUpTopBar(
            parentName = state.parentInfo.parentName,
            profileImage = state.parentInfo.parentProfileImage,
            onClickProfile = onLogOut
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "아직 연결된 자녀계정이 없어요\n" +
                    "자녀를 추가해주세요!",
            style = KieroTheme.typography.semiBold.title2,
            color = KieroTheme.colors.white,
            maxLines = 2
        )

        content()
    }
}

@Preview
@Composable
private fun ParentSignInEntryPreview() {
    KieroTheme {
        ParentSignInEntryScreen(
            paddingValues = PaddingValues(),
            state = ParentSignUpState(
                currentStep = ParentSignUpStep.ADDCHILD
            ),
            content = {},
            onLogOut = {}
        )
    }
}
