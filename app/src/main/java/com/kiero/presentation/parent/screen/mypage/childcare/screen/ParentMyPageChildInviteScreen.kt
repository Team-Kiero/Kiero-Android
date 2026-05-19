package com.kiero.presentation.parent.screen.mypage.childcare.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.screen.mypage.childcare.ParentMyPageChildCareState
import com.kiero.presentation.parent.screen.mypage.childcare.component.ParentMyPageChildNameHolder
import com.kiero.presentation.signup.parent.component.ParentSignUpInviteHolder

@Composable
fun ParentMyPageChildInviteScreen(
    paddingValues: PaddingValues,
    state: ParentMyPageChildCareState,
    onCopyClick: () -> Unit,
    onReIssueClick: () -> Unit,
) {
    Column (
        modifier = Modifier
            .padding(paddingValues)
    ) {
        ParentMyPageChildNameHolder(
            childInfo = state.childInfo
        )

        Spacer(modifier = Modifier.height(11.dp))

        ParentSignUpInviteHolder(
            code = state.childInfo.code,
            expiredTime = state.expiredTime,
            isExpired = state.isExpired,
            onCopyClick = onCopyClick,
            onReIssueClick = onReIssueClick
        )
    }
}

@Preview
@Composable
private fun ParentMyPageChildInviteScreenPreview() {
    KieroTheme {
        ParentMyPageChildInviteScreen(
            paddingValues = PaddingValues(),
            state = ParentMyPageChildCareState(),
            onCopyClick = {},
            onReIssueClick = {}
        )
    }
}
