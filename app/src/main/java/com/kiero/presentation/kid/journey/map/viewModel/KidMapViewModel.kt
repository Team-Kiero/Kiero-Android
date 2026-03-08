package com.kiero.presentation.kid.journey.map.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.kiero.core.model.UiState
import com.kiero.presentation.kid.journey.map.navigation.Map
import com.kiero.presentation.kid.journey.map.state.KidMapState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class KidMapViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val map = savedStateHandle.toRoute<Map>()

    private val _state = MutableStateFlow<UiState<KidMapState>>(
        UiState.Success(
            KidMapState.FAKE.copy(date = map.date)
        )
    )
    val state: StateFlow<UiState<KidMapState>> = _state.asStateFlow()

}