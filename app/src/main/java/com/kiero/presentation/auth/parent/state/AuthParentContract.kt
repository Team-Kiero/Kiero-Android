package com.kiero.presentation.auth.parent.state

import androidx.compose.runtime.Immutable
import com.kiero.core.model.UiState
import com.kiero.presentation.auth.parent.model.AuthParentConsentsUiModel

@Immutable
data class AuthParentState(
    val uiState: UiState<Unit> = UiState.Empty,
    val consents: AuthParentConsentsUiModel = AuthParentConsentsUiModel(),
    val isShowTermsAgreement: Boolean = false
)
