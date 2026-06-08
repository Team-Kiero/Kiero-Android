package com.kiero.presentation.parent.screen.mypage.childcare

import android.content.ClipData
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.core.common.extension.collectSingleEvent
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.component.indicator.KieroLoadingIndicator
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.trigger.SnackbarState
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.presentation.parent.screen.mypage.childcare.model.ParentChildCareStep
import com.kiero.presentation.parent.screen.mypage.childcare.screen.ParentMyPageChildInviteScreen
import com.kiero.presentation.parent.screen.mypage.childcare.screen.ParentMyPageChildManagementScreen

@Composable
fun ParentMyPageChildCareScreen(
    paddingValues: PaddingValues,
    navigateToMyPage: () -> Unit,
    viewModel: ParentMyPageChildCareViewModel = hiltViewModel()
) {
    val clipboardManager = LocalClipboard.current
    val globalTrigger = LocalGlobalUiEventTrigger.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.sideEffect.collectSingleEvent {
        when(it) {
            is ParentMyPageChildCareSideEffect.CopyText -> {
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

            is ParentMyPageChildCareSideEffect.ShowSnackbar -> {
                globalTrigger.showSnackbar(
                    SnackbarState(
                        message = it.message,
                        bottomPadding = 110
                    )
                )
            }

            ParentMyPageChildCareSideEffect.NavigateToMyPage -> navigateToMyPage()
        }
    }

    BackHandler {
        viewModel.onBackClick()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
            .padding(top = paddingValues.calculateTopPadding())
            .padding(horizontal = 16.dp, vertical = 13.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            KieroTopbar(
                title = "자녀 관리",
                leftIconClick = viewModel::onBackClick
            )

            if (state.isInitialized) {
                when(state.currentStep) {
                    ParentChildCareStep.MANAGEMENT -> {
                        ParentMyPageChildManagementScreen(
                            state = state,
                            onReIssueClick = viewModel::onReIssueClick,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    ParentChildCareStep.INVITE -> {
                        ParentMyPageChildInviteScreen(
                            state = state,
                            onCopyClick = viewModel::onCopyClick,
                            onReIssueClick = viewModel::onReIssueClick,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        if (!state.isInitialized || state.isLoading) {
            KieroLoadingIndicator()
        }
    }
}

@Preview
@Composable
private fun ParentMyPageChildCareScreenPreview() {
    KieroTheme {
        ParentMyPageChildCareScreen(
            paddingValues = PaddingValues(),
            navigateToMyPage = {}
        )
    }
}
