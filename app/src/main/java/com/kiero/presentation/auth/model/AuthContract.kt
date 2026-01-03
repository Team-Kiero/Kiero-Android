package com.kiero.presentation.auth.model

import androidx.compose.runtime.Immutable
import com.kiero.core.model.UiState
import com.kiero.data.auth.model.DummyEntity
import kotlinx.collections.immutable.PersistentList


@Immutable
data class DummyState(
    val uiState: UiState<PersistentList<DummyEntity>> = UiState.Loading,
)

sealed class DummySideEffect {
    data class ShowSnackBar(val message: String) : DummySideEffect()
    data object NavigateUp : DummySideEffect()
    data object NavigateNext : DummySideEffect()
}
