package com.kiero.presentation.kid.mission

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.common.extension.updateSuccess
import com.kiero.core.model.UiState
import com.kiero.data.mission.repository.MissionRepository
import com.kiero.presentation.kid.mission.model.toUiModel
import com.kiero.presentation.kid.mission.state.KidMissionSideEffect
import com.kiero.presentation.kid.mission.state.KidMissionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KidMissionViewModel @Inject constructor(
    private val missionRepository: MissionRepository
) : ViewModel() {
    private val _state = MutableStateFlow<UiState<KidMissionState>>(UiState.Loading)
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<KidMissionSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()


    init {
        fetchMissions()
    }

    private fun fetchMissions() {
        viewModelScope.launch {
            missionRepository.getMissions()
                .onSuccess { result ->
                    _state.updateSuccess {
                        it.copy(
                            kidMissionByDateList = result.toUiModel()
                        )
                    }
                }
                .onFailure {
                    _sideEffect.emit(KidMissionSideEffect.ShowSnackbar(it.message.toString()))
                }
        }
    }

    fun onMissionCompleted(missionId: Long) {
        viewModelScope.launch {
            missionRepository.patchMission(missionId)
                .onSuccess {
                    _state.updateSuccess { currentState ->
                        val updatedGroups = currentState.kidMissionByDateList.missionsByDate.map { group ->
                            group.copy(
                                missions = group.missions.map { mission ->
                                    if (mission.id == missionId) {
                                        mission.copy(isCompleted = true)
                                    } else {
                                        mission
                                    }
                                }.toImmutableList()
                            )
                        }.toImmutableList()

                        currentState.copy(
                            kidMissionByDateList = currentState.kidMissionByDateList.copy(
                                missionsByDate = updatedGroups
                            )
                        )
                    }
                }
                .onFailure { exception ->
                    _sideEffect.emit(KidMissionSideEffect.ShowSnackbar(exception.message.toString()))
                }
        }
    }
}