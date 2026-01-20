package com.kiero.presentation.parent.schedule.mission.auto

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiero.R
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.mission.auto.component.ScrollableAutoInputField
import com.kiero.presentation.parent.schedule.mission.auto.viewmodel.AutoMissionViewModel

@Composable
fun ParentAutoAddRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    childId: Long,
    viewModel: AutoMissionViewModel = hiltViewModel()
) {
    val noticeText by viewModel.noticeText.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val missions by viewModel.missions.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.shouldNavigateBack.collect {
            navigateUp()
        }
    }

    when {
        isAnalyzing -> ParentAutoLoadingScreen(
            paddingValues = paddingValues,
            snackbarHostState = snackbarHostState
        )

        missions.isNotEmpty() -> ParentAutoResultScreen(
            paddingValues = paddingValues,
            viewModel = viewModel,
            childId = childId,
            navigateUp = navigateUp
        )

        else -> ParentAutoAddScreen(
            paddingValues = paddingValues,
            navigateUp = { viewModel.handleCancel() },
            noticeText = noticeText,
            isAnalyzeEnabled = noticeText.length >= 10,
            onTextChange = { viewModel.updateNoticeText(it) },
            onAnalyzeClick = { viewModel.analyzeNotice() }
        )
    }
}
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ParentAutoAddScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    noticeText: String,
    isAnalyzeEnabled: Boolean,
    onTextChange: (String) -> Unit,
    onAnalyzeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val isImeVisible = WindowInsets.isImeVisible

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
            .imePadding()
            .padding(paddingValues)
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
            leftIconClick = navigateUp
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
            Spacer(Modifier.height(48.dp))

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
private fun ParentAutoAddScreen_EmptyPreview() {
    KieroTheme {
        ParentAutoAddScreen(
            paddingValues = PaddingValues(),
            navigateUp = {},
            noticeText = "",
            isAnalyzeEnabled = false,
            onTextChange = {},
            onAnalyzeClick = {}
        )
    }
}

@Preview
@Composable
private fun ParentAutoAddScreen_FilledPreview() {
    KieroTheme {
        ParentAutoAddScreen(
            paddingValues = PaddingValues(),
            navigateUp = {},
            noticeText = "내일은 체육복을 꼭 챙겨오세요. 수학 숙제 26~30페이지를 풀어오세요.",
            isAnalyzeEnabled = true,
            onTextChange = {},
            onAnalyzeClick = {}
        )
    }
}