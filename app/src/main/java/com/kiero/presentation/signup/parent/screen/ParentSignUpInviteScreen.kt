package com.kiero.presentation.signup.parent.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.component.KieroTextField
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.signup.parent.component.ParentSignUpInviteHolder
import com.kiero.presentation.signup.parent.state.ParentSignUpState

@Composable
fun ParentSignUpInviteScreen(
    state: ParentSignUpState,
    onStartClick: () -> Unit,
    onCopyClick: () -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(38.dp))

        KieroTextField(
            state = TextFieldState(state.childInfo.childFirstName.text.toString()),
            placeholder = "",
            isError = false,
            enabled = false,
            containerColor = KieroTheme.colors.gray900,
        )

        Spacer(modifier = Modifier.height(28.dp))

        ParentSignUpInviteHolder(
            code = state.childInfo.code,
            expiredTime = state.expiredTime,
            onCopyClick = onCopyClick
        )

        Spacer(modifier = Modifier.weight(1f))

        // Todo : SSE 이벤트로 이벤트 타입 받으면 수정하기
        KieroButtonMedium(
            text = "시작하기",
            onClick = onStartClick,
            containerColor = KieroTheme.colors.gray900,
            contentColor = KieroTheme.colors.white
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
            onCopyClick = {}
        )
    }
}