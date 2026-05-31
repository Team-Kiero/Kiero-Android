package com.kiero.presentation.signup.parent.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.component.text.KieroTextHolder
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.signup.parent.component.ParentSignUpInviteHolder
import com.kiero.presentation.signup.parent.state.ParentSignUpState

@Composable
fun ParentSignUpInviteScreen(
    state: ParentSignUpState,
    onStartClick: () -> Unit,
    onCopyClick: () -> Unit,
    onReIssueClick: () -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(38.dp))

        KieroTextHolder(
            text = "${state.childInfo.childLastName.text}${state.childInfo.childFirstName.text}"
        )

        Spacer(modifier = Modifier.height(28.dp))

        ParentSignUpInviteHolder(
            code = state.childInfo.code,
            expiredTime = state.expiredTime,
            isExpired = state.isExpired,
            onCopyClick = onCopyClick,
            onReIssueClick = onReIssueClick
        )

        Spacer(modifier = Modifier.weight(1f))

        KieroButtonMedium(
            text = "시작하기",
            onClick = onStartClick,
            isEnabled = state.isChildJoined,
            containerColor = KieroTheme.colors.main,
            contentColor = KieroTheme.colors.black
        )
    }
}

@Preview
@Composable
private fun ParentSignUpInvitePreview() {
    KieroTheme {
        ParentSignUpInviteScreen(
            state = ParentSignUpState(),
            onStartClick = {},
            onCopyClick = {},
            onReIssueClick = {}
        )
    }
}
