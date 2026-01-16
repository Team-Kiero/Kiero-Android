package com.kiero.presentation.parent.schedule.mission.auto

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiero.R
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.mission.auto.component.ParentAutoInputField
import com.kiero.presentation.parent.schedule.mission.auto.state.AutoMissionContract
import com.kiero.presentation.parent.schedule.mission.auto.state.AutoMissionEvent
import com.kiero.presentation.parent.schedule.mission.auto.state.AutoMissionSideEffect
import com.kiero.presentation.parent.schedule.mission.auto.viewmodel.AutoMissionViewModel

// Todo : 네비게이션 연결
@Composable
fun ParentAutoAddRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    val viewModel: AutoMissionViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // State 기반 화면 이동
    LaunchedEffect(state.shouldNavigateBack) {
        if (state.shouldNavigateBack) {
            navigateUp()
        }
    }

    // SideEffect 처리 (Toast, ScrollToPage)
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is AutoMissionSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                is AutoMissionSideEffect.ScrollToPage -> {
                    // TODO: HorizontalPager와 연동
                }
            }
        }
    }

    when (state.currentScreen) {
        AutoMissionContract.Screen.INPUT -> ParentAutoAddScreen(
            paddingValues = paddingValues,
            navigateUp = { viewModel.onEvent(AutoMissionEvent.OnCancelClick) },
            state = state,
            onEvent = viewModel::onEvent,
        )
        AutoMissionContract.Screen.LOADING -> ParentAutoLoadingScreen(
            paddingValues = paddingValues,
        )
        AutoMissionContract.Screen.RESULT -> ParentAutoResultScreen(
            paddingValues = paddingValues,
            state = state,
            onEvent = viewModel::onEvent,
            navigateUp = { viewModel.onEvent(AutoMissionEvent.OnCancelClick) }  // ✅ 이벤트로
        )
    }
}

@Composable
fun ParentAutoAddScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    state: AutoMissionContract,
    onEvent: (AutoMissionEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = KieroTheme.colors.black)
            .padding(paddingValues)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        KieroTopbar(
            title = "알림장 미션 추가",
            leftIconRes = R.drawable.ic_close_light,
            leftIconClick = navigateUp
        )

        Text(
            text = "이곳에 알림장 내용을 붙여넣어 주세요.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 24.dp,
                    start = 20.dp,
                    end = 20.dp
                ),
            style = KieroTheme.typography.semiBold.title3,
            color = KieroTheme.colors.gray200
        )

        ParentAutoInputField(
            text = state.noticeText,
            onTextChange = { onEvent(AutoMissionEvent.OnNoticeTextChanged(it)) },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        KieroButtonMedium(
            text = "분석하고 미션 추가하기",
            onClick = { onEvent(AutoMissionEvent.OnAnalyzeClick) },
            isEnabled = state.isAnalyzeEnabled,
            containerColor = KieroTheme.colors.main,
            contentColor = KieroTheme.colors.black,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Preview
@Composable
private fun ParentAutoAddScreenPreview() {
    KieroTheme {
        ParentAutoAddScreen(
            paddingValues = PaddingValues(),
            navigateUp = {},
            state = AutoMissionContract(),
            onEvent = {},
        )
    }
}

@Preview
@Composable
private fun ParentAutoAddScreenWithLongTextPreview() {
    KieroTheme {
        ParentAutoAddScreen(
            paddingValues = PaddingValues(),
            navigateUp = {},
            state = AutoMissionContract(
                noticeText = """
                    안녕하세요. 내일 준비물 안내드립니다.
                    
                    1. 독서록 제출하기
                    2. 수학 익힘책 30-35쪽 풀어오기
                    3. 과학 준비물: 페트병, 풍선
                    4. 미술 준비물: 스케치북, 색연필
                    
                    감사합니다.
                """.trimIndent()
            ),
            onEvent = {},
        )
    }
}