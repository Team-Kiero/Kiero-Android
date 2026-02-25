package com.kiero.presentation.parent.screen.mission.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.model.UiState
import com.kiero.data.parent.mission.repository.MissionRepository
import com.kiero.presentation.kid.mission.model.toUiModel
import com.kiero.presentation.parent.screen.mission.state.ParentMissionSideEffect
import com.kiero.presentation.parent.screen.mission.state.ParentMissionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParentMissionViewModel @Inject constructor(
    private val missionRepository: MissionRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<UiState<ParentMissionState>>(UiState.Loading)
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ParentMissionSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun getMissions() {
        viewModelScope.launch {
            missionRepository.getMissions()
                .onSuccess { result ->
                    if (result.missionsByDate.isEmpty()) {
                        _state.value = UiState.Empty
                    } else {
                        _state.value = UiState.Success(
                            ParentMissionState(
                                kidMissionByDateList = result.toUiModel()
                            )
                        )
                    }
                }
                .onFailure {
                    _state.value = UiState.Failure(it.message.toString())
                    _sideEffect.emit(
                        ParentMissionSideEffect.ShowSnackbar(it.message.toString())
                    )
                }
        }
    }
}