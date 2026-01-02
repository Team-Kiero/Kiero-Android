package com.Kiero.presentation.auth.model

import androidx.compose.runtime.Immutable
import com.Kiero.core.model.UiState
import com.Kiero.data.auth.model.DummyEntity
import kotlinx.collections.immutable.PersistentList

class AuthContract {
    @Immutable
    data class DummyState(
        val uiState: UiState<PersistentList<DummyEntity>> = UiState.Loading,
    )

    sealed class DummySideEffect {
        data class ShowSnackBar(val message: String) : DummySideEffect()
        data object NavigateUp : DummySideEffect()
        data object NavigateNext : DummySideEffect()
    }
}