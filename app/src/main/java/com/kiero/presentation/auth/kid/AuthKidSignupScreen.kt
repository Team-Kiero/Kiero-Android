package com.kiero.presentation.auth.kid

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.kiero.presentation.auth.kid.component.KidInputField


@Composable
fun AuthKidSignupRoute(
    paddingValues: PaddingValues,
    navigateToKidOnboarding: () -> Unit,
    navigateUp: () -> Unit = {},
    viewmodel: KidSignupViewModel = hiltViewModel(),
) {
    val firstNameState = TextFieldState()
    val lastNameState = TextFieldState()
    val inviteCodeState = TextFieldState()

    val eventTrigger = LocalGlobalUiEventTrigger.current

    viewmodel.sideEffect.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is KidSideEffect.ShowSnackBar -> {
                eventTrigger.showSnackbar(
                    SnackbarState(message = sideEffect.message)
                )
            }

        }
    }

    AuthKidSignupScreen(
        paddingValues = paddingValues,
        navigateToKidOnboarding = navigateToKidOnboarding,
        firstNameState = firstNameState,
        lastNameState = lastNameState,
        inviteCodeState = inviteCodeState,
        onSignupClick = {
            viewmodel.onSignupClick(
                firstName = firstNameState.text.toString(),
                lastName = lastNameState.text.toString(),
                inviteCode = inviteCodeState.text.toString()
            )
        }
    )
}

@Composable
fun AuthKidSignupScreen(
    paddingValues: PaddingValues,
    navigateToKidOnboarding: () -> Unit,
    firstNameState: TextFieldState,
    lastNameState: TextFieldState,
    inviteCodeState: TextFieldState,
    onSignupClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = KieroTheme.colors.black
            )
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(21.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        //TODO : 공통 컴포넌트 수정한 거 적용(우측 아이콘)
        KieroTopbar(
            title = "자녀로 시작하기",
            leftIconRes = R.drawable.ic_arrow_left,
            leftIconClick = {},
            rightIconRes = R.drawable.ic_arrow_right,
            rightIconClick = {}
        )

        Text(
            text = "이름과 부모님께 받은 초대 코드를 입력해줘!",
            color = KieroTheme.colors.gray200,
            style = KieroTheme.typography.semiBold.title3,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 15.dp)
        )

        KidInputField(
            fieldTitle = "성",
            fieldInputText = "성을 입력해줘!",
            fieldState = firstNameState,
        )

        KidInputField(
            fieldTitle = "이름",
            fieldInputText = "이름을 입력해줘!",
            fieldState = lastNameState
        )

        Spacer(modifier = Modifier.height(32.dp))

        KidInputField(
            fieldTitle = "초대 코드",
            fieldInputText = "부모님께 받은 비밀 암호를 입력해줘!",
            fieldState = inviteCodeState
        )

        Spacer(modifier = Modifier.weight(1f))

        KieroButtonMedium(
            text = "여정 시작하기",
            onClick = navigateToKidOnboarding,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(41.dp))
    }
}

@Preview
@Composable
private fun KidSignupScreenPreview() {
    KieroTheme {
        AuthKidSignupScreen(
            navigateToKidOnboarding = {},
            firstNameState = TextFieldState(),
            lastNameState = TextFieldState(),
            inviteCodeState = TextFieldState(),
            onSignupClick = {},
            paddingValues = PaddingValues()
        )
    }
}