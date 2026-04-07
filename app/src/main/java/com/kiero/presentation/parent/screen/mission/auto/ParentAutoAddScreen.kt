package com.kiero.presentation.parent.screen.mission.auto

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kiero.R
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.trigger.SnackbarState
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.presentation.parent.screen.mission.auto.component.ScrollableAutoInputField
import com.kiero.presentation.parent.screen.mission.auto.state.AutoMissionSideEffect
import com.kiero.presentation.parent.screen.mission.auto.state.AutoMissionState
import com.kiero.presentation.parent.screen.mission.auto.viewmodel.AutoMissionViewModel
import timber.log.Timber

@Composable
fun ParentAutoAddRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: AutoMissionViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val globalUiEventHolder = LocalGlobalUiEventTrigger.current
    val focusManager = LocalFocusManager.current

    viewModel.sideEffect.collectSideEffect { effect ->
        when (effect) {
            is AutoMissionSideEffect.ShowToast -> {
                focusManager.clearFocus()
                globalUiEventHolder.showSnackbar(SnackbarState(message = effect.message))
            }

            is AutoMissionSideEffect.NavigateBack -> {
                navigateUp()
            }

            is AutoMissionSideEffect.ScrollToPage -> Unit

            is AutoMissionSideEffect.ShowToastAndNavigate -> {
                Timber.e("parent auto add")
                focusManager.clearFocus()
                globalUiEventHolder.showSnackbar(SnackbarState(message = effect.message))
                navigateUp()
            }
        }
    }

    when (state.currentScreen) {
        AutoMissionState.Screen.LOADING -> {
            ParentAutoLoadingScreen(paddingValues = paddingValues)
        }

        AutoMissionState.Screen.RESULT -> {
            ParentAutoResultRoute(
                paddingValues = paddingValues,
                state = state,
                viewModel = viewModel,
                navigateUp = navigateUp
            )
        }

        AutoMissionState.Screen.INPUT -> {
            ParentAutoAddScreen(
                state = state,
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
    state: AutoMissionState,
    onTextChange: (String) -> Unit,
    onAnalyzeClick: () -> Unit,
    onCancelClick: () -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    // 🌟 커서 위치를 기억하는 TextFieldValue 상태 생성
    var noticeTextFieldValue by remember {
        mutableStateOf(TextFieldValue(text = state.noticeText))
    }

    // ViewModel의 상태와 UI의 TextFieldValue 동기화 (외부에서 텍스트가 강제로 변경될 때 대비)
    LaunchedEffect(state.noticeText) {
        if (state.noticeText != noticeTextFieldValue.text) {
            noticeTextFieldValue = noticeTextFieldValue.copy(text = state.noticeText)
        }
    }

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
                .padding(top = 24.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
            style = KieroTheme.typography.semiBold.title3,
            color = KieroTheme.colors.gray200
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            ScrollableAutoInputField(
                value = noticeTextFieldValue,
                onValueChange = { newValue ->
                    noticeTextFieldValue = newValue
                    onTextChange(newValue.text)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(12.dp))

        KieroButtonMedium(
            text = "분석하고 미션 추가하기",
            onClick = onAnalyzeClick,
            isEnabled = state.isAnalyzeEnabled,
            containerColor = KieroTheme.colors.main,
            contentColor = KieroTheme.colors.black,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(28.dp))
    }
}


@Preview
@Composable
private fun ParentAutoAddScreen_EmptyPreview() {
    KieroTheme {
        ParentAutoAddScreen(
            state = AutoMissionState(),
            onTextChange = {},
            onAnalyzeClick = {},
            onCancelClick = {},
            paddingValues = PaddingValues()
        )
    }
}

@Preview
@Composable
private fun ParentAutoAddScreen_FilledPreview() {
    KieroTheme {
        ParentAutoAddScreen(
            state = AutoMissionState(
                noticeText = "내일은 체육복을 꼭 챙겨오세요. 수학 숙제 26~30페이지를 풀어오세요."
            ),
            onTextChange = {},
            onAnalyzeClick = {},
            onCancelClick = {},
            paddingValues = PaddingValues()
        )
    }
}