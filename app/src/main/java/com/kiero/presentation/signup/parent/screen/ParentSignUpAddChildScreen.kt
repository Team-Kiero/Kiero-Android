package com.kiero.presentation.signup.parent.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.signup.parent.component.ParentSignUpForm
import com.kiero.presentation.signup.parent.state.ParentSignUpState


@Composable
fun ParentSignUpAddChildScreen(
    state: ParentSignUpState,
    onNextClick: () -> Unit,
    nextFocus: () -> Unit,
    doneFocus: () -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(17.dp))

        ParentSignUpForm(
            title = "아이의 성을 입력해주세요.",
            placeholder = "성",
            textState = state.childInfo.childLastName,
            isError = state.childInfo.childLastName.text.isNotEmpty() && !state.childInfo.validateLastName,
            onImeAction = nextFocus,
        )

        Spacer(modifier = Modifier.height(18.dp))

        ParentSignUpForm(
            title = "아이의 이름을 입력해주세요.",
            placeholder = "이름",
            textState = state.childInfo.childFirstName,
            isError = state.childInfo.childFirstName.text.isNotEmpty() && !state.childInfo.validateFirstName,
            onImeAction = doneFocus,
            imeAction = ImeAction.Done
        )

        Spacer(modifier = Modifier.weight(1f))

        KieroButtonMedium(
            text = "초대코드 생성",
            isEnabled = state.childInfo.validateFirstName && state.childInfo.validateLastName,
            onClick = onNextClick
        )
    }
}

@Preview
@Composable
private fun ParentSignUpScreenPreview() {
    KieroTheme {
        ParentSignUpAddChildScreen(
            state = ParentSignUpState(),
            onNextClick = {},
            nextFocus = {},
            doneFocus = {}
        )
    }
}