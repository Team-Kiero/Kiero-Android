package com.kiero.presentation.auth.kid.state

import androidx.compose.runtime.Immutable
import com.kiero.presentation.auth.kid.model.KidSignUpUiModel

@Immutable
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