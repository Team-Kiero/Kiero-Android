package com.kiero.presentation.kid.journey.fire.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.kiero.core.model.UiState
import com.kiero.presentation.kid.journey.fire.navigation.Fire
import com.kiero.presentation.kid.journey.fire.state.KidFireState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class KidFireViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val fire = savedStateHandle.toRoute<Fire>()

    private val _fireState = MutableStateFlow<UiState<KidFireState>>(
        UiState.Success(
            KidFireState(
                date = fire.date,
                stones = fire.stones
            )
        )
    )
    val fireState = _fireState.asStateFlow()
}