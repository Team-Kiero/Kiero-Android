package com.kiero.presentation.kid.journey.viewmodel

import androidx.lifecycle.ViewModel
import com.kiero.presentation.kid.journey.state.KidJourneySideEffect
import com.kiero.presentation.kid.journey.state.KidJourneyState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class KidJourneyViewModel @Inject constructor(
) : ViewModel() {
    private val _state = MutableStateFlow(KidJourneyState.fake())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<KidJourneySideEffect>()
    val sideEffect: SharedFlow<KidJourneySideEffect> = _sideEffect.asSharedFlow()
}
