package com.kiero.presentation.parent.screen.mypage.childcare.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.screen.mypage.childcare.component.ParentMyPageChildNameHolder
import com.kiero.presentation.parent.screen.mypage.childcare.model.ParentMyPageChildInfoModel

@Composable
fun ParentMyPageChildManagementScreen(
    paddingValues: PaddingValues,
    state: ParentMyPageChildInfoModel,
    onReIssueClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ParentMyPageChildNameHolder(
            childInfo = state
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "아이가 다시 로그인해야 하는 경우, 연결 코드를 새로 발급해주세요.",
            style = KieroTheme.typography.regular.body4,
            color = KieroTheme.colors.gray300
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
            paddingValues = PaddingValues(),
            state = ParentMyPageChildInfoModel(),
            onReIssueClick = {}
        )
    }
}
