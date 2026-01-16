package com.kiero.presentation.signup.parent.state

import androidx.compose.runtime.Immutable
import com.kiero.presentation.signup.parent.model.ParentInfoUiModel
import com.kiero.presentation.signup.parent.model.ParentSignUpChildInfoUiModel
import com.kiero.presentation.signup.parent.model.ParentSignUpStep

@Immutable
data class ParentSignUpState(
    val parentInfo : ParentInfoUiModel = ParentInfoUiModel(),
    val childInfo: ParentSignUpChildInfoUiModel = ParentSignUpChildInfoUiModel(),
    val expiredTime: String = "",
    val currentStep: ParentSignUpStep = ParentSignUpStep.ADDCHILD,
    val isLogoutDialogVisible: Boolean = false
)

sealed interface ParentSignUpSideEffect {
    data object NavigateToParent : ParentSignUpSideEffect
    data object NavigateToSelection : ParentSignUpSideEffect

    data class CopyText(val message: String, val targetText: String) : ParentSignUpSideEffect

    data class ShowSnackbar(val message: String) : ParentSignUpSideEffect
}
