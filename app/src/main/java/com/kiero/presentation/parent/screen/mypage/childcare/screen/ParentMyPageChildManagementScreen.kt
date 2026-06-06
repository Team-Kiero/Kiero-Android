package com.kiero.presentation.parent.screen.mypage.childcare.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.screen.mypage.childcare.ParentMyPageChildCareState
import com.kiero.presentation.parent.screen.mypage.childcare.component.ParentMyPageChildNameHolder

@Composable
fun ParentMyPageChildManagementScreen(
    state: ParentMyPageChildCareState,
    onReIssueClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ParentMyPageChildNameHolder(
            childInfo = state.childInfo,
            connectionStatus = state.connectionStatus
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "아이가 다시 로그인해야 하는 경우, 연결 코드를 새로 발급해주세요.",
            style = KieroTheme.typography.regular.body4,
            color = KieroTheme.colors.gray300,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(24.dp))

        KieroButtonMedium(
            text = "연결 코드 재발급",
            onClick = onReIssueClick,
            containerColor = KieroTheme.colors.main,
            contentColor = KieroTheme.colors.black
        )

        Spacer(modifier = Modifier.height(17.dp))
    }
}

@Preview
@Composable
private fun ParentMyPageChildManagementScreenPreview() {
    KieroTheme {
        ParentMyPageChildManagementScreen(
            state = ParentMyPageChildCareState(),
            onReIssueClick = {}
        )
    }
}
