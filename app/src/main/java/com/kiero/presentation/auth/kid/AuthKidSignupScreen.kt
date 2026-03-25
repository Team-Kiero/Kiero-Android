package com.kiero.presentation.auth.kid

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.R
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.common.util.MaxLengthInputTransformation
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.trigger.SnackbarState
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.presentation.auth.kid.component.KidInputField
import com.kiero.presentation.auth.kid.state.KidSignUpState
import com.kiero.presentation.auth.kid.state.KidSignupSideEffect


@Composable
fun AuthKidSignupRoute(
    paddingValues: PaddingValues,
    navigateToKidOnboarding: () -> Unit,
    navigateUp: () -> Unit = {},
    viewmodel: KidSignupViewModel = hiltViewModel(),
) {
    val focusManager = LocalFocusManager.current
    val eventTrigger = LocalGlobalUiEventTrigger.current
    val state by viewmodel.state.collectAsStateWithLifecycle()

    viewmodel.sideEffect.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is KidSignupSideEffect.ShowSnackbar -> {
                eventTrigger.showSnackbar(
                    SnackbarState(
                        message = sideEffect.message,
                        bottomPadding = 130
                    )
                )
            }

            KidSignupSideEffect.NavigateToKidOnboarding -> navigateToKidOnboarding()

            KidSignupSideEffect.ShowDialog -> {}
        }
    }

    AuthKidSignupScreen(
        paddingValues = paddingValues,
        state = state,
        onSignupClick = viewmodel::onSignupClick,
        onDone = {
            focusManager.clearFocus()
        },
        nextFocus = {
            focusManager.moveFocus(FocusDirection.Down)
        },
        navigateUp = navigateUp
    )
}

@Composable
fun AuthKidSignupScreen(
    paddingValues: PaddingValues,
    state: KidSignUpState,
    onSignupClick: () -> Unit,
    nextFocus: () -> Unit,
    onDone: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    with(state.kidSignUpUiModel) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(
                    color = KieroTheme.colors.black
                )
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    })
                }
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            KieroTopbar(
                title = "자녀로 시작하기",
                leftIconRes = R.drawable.ic_arrow_left,
                leftIconClick = navigateUp,
            )

            Spacer(modifier = Modifier.height(21.dp))

            Text(
                text = "이름과 부모님께 받은 초대 코드를 입력해줘!",
                color = KieroTheme.colors.gray200,
                style = KieroTheme.typography.semiBold.title3,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 15.dp)
            )

            Spacer(modifier = Modifier.height(31.dp))

            KidInputField(
                fieldTitle = "성",
                fieldInputText = "성을 입력해줘!",
                fieldState = lastName,
                isError = state.kidSignUpUiModel.lastName.text.isNotEmpty() && !validateLastName,
                onImeAction = nextFocus,
                imeAction = ImeAction.Next,
                inputTransformation = MaxLengthInputTransformation(5)
            )

            KidInputField(
                fieldTitle = "이름",
                fieldInputText = "이름을 입력해줘!",
                fieldState = firstName,
                isError = state.kidSignUpUiModel.firstName.text.isNotEmpty() && !validateFirstName,
                onImeAction = nextFocus,
                imeAction = ImeAction.Next,
                inputTransformation = MaxLengthInputTransformation(5)
            )

            Spacer(modifier = Modifier.height(31.dp))

            KidInputField(
                fieldTitle = "초대 코드",
                fieldInputText = "부모님께 받은 비밀 암호를 입력해줘!",
                fieldState = inviteCode,
                onImeAction = onDone,
                imeAction = ImeAction.Done
            )

            Spacer(modifier = Modifier.weight(1f))

            KieroButtonMedium(
                text = "여정 시작하기",
                onClick = onSignupClick,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(55.dp))
        }
    }
}

@Preview
@Composable
private fun KidSignupScreenPreview() {
    KieroTheme {
        AuthKidSignupScreen(
            state = KidSignUpState(),
            onSignupClick = {},
            paddingValues = PaddingValues(),
            nextFocus = {},
            onDone = {},
            navigateUp = {}
        )
    }
}
