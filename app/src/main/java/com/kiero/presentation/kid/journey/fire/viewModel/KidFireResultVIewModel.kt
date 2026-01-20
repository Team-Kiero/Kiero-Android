package com.kiero.presentation.kid.journey.fire.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.data.schedule.repository.ScheduleRepository
import com.kiero.presentation.kid.journey.fire.model.toUiModel
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
    private val repository: ScheduleRepository
) : ViewModel() {
    private val _state = MutableStateFlow(KidFireResultState())
    val state = _state.asStateFlow()

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