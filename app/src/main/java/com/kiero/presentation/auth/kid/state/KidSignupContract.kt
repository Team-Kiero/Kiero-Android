package com.kiero.presentation.auth.kid.state

import androidx.compose.runtime.Stable
import com.kiero.presentation.auth.kid.model.KidSignUpUiModel

@Stable
data class KidSignUpState(
    val kidSignUpUiModel: KidSignUpUiModel = KidSignUpUiModel(),
)

sealed interface KidSignupSideEffect {
    data object ShowDialog : KidSignupSideEffect

    data class ShowSnackbar(
        val message: String,
    ) : KidSignupSideEffect

    data object NavigateToKidOnboarding : KidSignupSideEffect
}