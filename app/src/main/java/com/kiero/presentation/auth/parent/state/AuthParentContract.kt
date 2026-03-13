package com.kiero.presentation.auth.parent.state

import androidx.compose.runtime.Immutable
import com.kiero.core.model.UiState

@Immutable
data class AuthParentState(
    val uiState: UiState<Unit> = UiState.Empty
)
