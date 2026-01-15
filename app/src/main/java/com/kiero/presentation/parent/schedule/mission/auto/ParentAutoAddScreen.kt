package com.kiero.presentation.parent.schedule.mission.auto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.mission.auto.component.ParentAutoInputField
import com.kiero.presentation.parent.schedule.mission.auto.state.AutoMissionContract
import com.kiero.presentation.parent.schedule.mission.auto.state.AutoMissionEvent

// Todo : 네비게이션 연결
@Composable
fun ParentAutoAddRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    ParentAutoAddScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp,
        state = AutoMissionContract.FAKE,
        onEvent = { event -> /* TODO: 이벤트 처리 */ },
        onBack = navigateUp,
    )
}

@Composable
fun ParentAutoAddScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    state: AutoMissionContract,
    onEvent: (AutoMissionEvent) -> Unit,
    onBack: () -> Unit,
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
        Spacer(modifier = Modifier.height(25.dp))

        // TopBar
        KieroTopbar(
            title = "알림장 미션 추가",
            leftIconRes = R.drawable.ic_close_light,
            leftIconClick = navigateUp
        )

        // 라벨
        Text(
            text = "이곳에 알림장 내용을 붙여넣어 주세요.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 19.dp,
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
            modifier = Modifier.padding(horizontal = 20.dp)
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
            onBack = {}
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
            onBack = {}
        )
    }
}