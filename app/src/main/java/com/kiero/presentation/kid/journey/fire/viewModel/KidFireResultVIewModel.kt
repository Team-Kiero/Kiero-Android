package com.kiero.presentation.kid.journey.fire.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kiero.data.kid.schedule.repository.ScheduleRepository
import com.kiero.presentation.kid.journey.fire.model.toUiModel
import com.kiero.presentation.kid.journey.fire.navigation.FireResult
import com.kiero.presentation.kid.journey.fire.state.KidFireResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class KidFireResultVIewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ScheduleRepository
) : ViewModel() {
    private val fireResult = savedStateHandle.toRoute<FireResult>()

    private val _state = MutableStateFlow(
        KidFireResultState(
            date = fireResult.date
        )
    )

    val fireResultState = _state.asStateFlow()

    init {
        fetchFireResult()
    }

    private fun fetchFireResult() {
        viewModelScope.launch {
            repository.patchScheduleFireLit()
                .onSuccess { fireModel ->
                    _state.update { currentState ->
                        currentState.copy(
                            content = fireModel.toUiModel()
                        )
                    }
                }
                .onFailure {
                    Timber.e("불 피우기 실패: $it")
                }
        }
    }
}