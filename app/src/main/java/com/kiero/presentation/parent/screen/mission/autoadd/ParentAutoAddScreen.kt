package com.kiero.presentation.parent.screen.mission.autoadd

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kiero.R
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.presentation.parent.screen.mission.autoadd.component.ScrollableAutoInputField
import com.kiero.presentation.parent.screen.mission.autoadd.state.AutoMissionSideEffect
import com.kiero.presentation.parent.screen.mission.autoadd.state.AutoMissionState
import com.kiero.presentation.parent.screen.mission.autoadd.viewmodel.AutoMissionViewModel
import timber.log.Timber

@Composable
fun ParentAutoAddRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: AutoMissionViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }


    viewModel.sideEffect.collectSideEffect { effect ->
        when (effect) {
            is AutoMissionSideEffect.ShowToast -> {
                snackbarHostState.showSnackbar(effect.message)
            }

            is AutoMissionSideEffect.NavigateBack -> {
                navigateUp()
            }

            is AutoMissionSideEffect.ScrollToPage -> {
            }

            is AutoMissionSideEffect.ShowToastAndNavigate -> {
                Timber.e("parent auto add")
                snackbarHostState.showSnackbar(effect.message)
                navigateUp()
            }
        }
    }

    when (state.currentScreen) {
        AutoMissionState.Screen.LOADING -> {
            ParentAutoLoadingScreen(
                paddingValues = paddingValues,
                snackbarHostState = snackbarHostState
            )
        }

        AutoMissionState.Screen.RESULT -> {
            ParentAutoResultRoute(
                paddingValues = paddingValues,
                state = state,
                viewModel = viewModel,
                snackbarHostState = snackbarHostState,
                navigateUp = navigateUp
            )
        }

        AutoMissionState.Screen.INPUT -> {
            ParentAutoAddScreen(
                noticeText = state.noticeText,
                isAnalyzeEnabled = state.isAnalyzeEnabled,
                onTextChange = viewModel::updateNoticeText,
                onAnalyzeClick = viewModel::analyzeNotice,
                onCancelClick = viewModel::handleCancel,
                paddingValues = paddingValues
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ParentAutoAddScreen(
    noticeText: String,
    isAnalyzeEnabled: Boolean,
    onTextChange: (String) -> Unit,
    onAnalyzeClick: () -> Unit,
    onCancelClick: () -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val isImeVisible = WindowInsets.isImeVisible

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
            .padding(paddingValues)
            .imePadding()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))

        KieroTopbar(
            title = "알림장 미션 추가",
            leftIconRes = R.drawable.ic_close_light,
            leftIconClick = onCancelClick
        )

        Text(
            text = "이곳에 알림장 내용을 붙여넣어 주세요.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 20.dp, end = 20.dp),
            style = KieroTheme.typography.semiBold.title3,
            color = KieroTheme.colors.gray200
        )

        ScrollableAutoInputField(
            text = noticeText,
            onTextChange = onTextChange,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
        )

        if (!isImeVisible) {

            KieroButtonMedium(
                text = "분석하고 미션 추가하기",
                onClick = onAnalyzeClick,
                isEnabled = isAnalyzeEnabled,
                containerColor = KieroTheme.colors.main,
                contentColor = KieroTheme.colors.black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(28.dp))
        }
    }
}


@Preview
@Composable
private fun ParentAutoAddScreen_FilledPreview() {
    KieroTheme {
        ParentAutoAddScreen(
            noticeText = "내일은 체육복을 꼭 챙겨오세요. 수학 숙제 26~30페이지를 풀어오세요.",
            isAnalyzeEnabled = true,
            onTextChange = {},
            onAnalyzeClick = {},
            onCancelClick = {},
            paddingValues = PaddingValues()
        )
    }
}