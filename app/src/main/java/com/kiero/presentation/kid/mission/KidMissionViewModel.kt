package com.kiero.presentation.kid.mission

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.common.extension.updateSuccess
import com.kiero.core.model.UiState
import com.kiero.data.kid.coin.repository.CoinRepository
import com.kiero.data.mission.repository.MissionRepository
import com.kiero.presentation.kid.mission.model.toUiModel
import com.kiero.presentation.kid.mission.state.KidMissionSideEffect
import com.kiero.presentation.kid.mission.state.KidMissionState
import com.kiero.presentation.kid.model.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class KidMissionViewModel @Inject constructor(
    repository: CoinRepository,
    private val missionRepository: MissionRepository
) : ViewModel() {
    private val _state = MutableStateFlow<UiState<KidMissionState>>(UiState.Loading)
    val state: StateFlow<UiState<KidMissionState>> = combine(
        _state,
        repository.myCoin
    ) { uiState, coinData ->
        when (uiState) {
            is UiState.Success -> {
                UiState.Success(
                    uiState.data.copy(
                        coinUiModel = coinData.toState()
                    )
                )
            }

            is UiState.Loading -> UiState.Loading
            is UiState.Failure -> UiState.Failure(uiState.message)
            is UiState.Empty -> UiState.Empty
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState.Loading
    )

    private val _sideEffect = MutableSharedFlow<KidMissionSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()


    init {
        fetchMissions()
    }

    fun fetchMissions(isRefreshing: Boolean = false) {
        viewModelScope.launch {
            if (isRefreshing) {
                _state.updateSuccess { it.copy(isRefreshing = true) }
                Timber.e("fetchMissions $isRefreshing")
            }
            val minLoadingTime = launch {
                if (isRefreshing) delay(1000)
            }

            missionRepository.getMissions()
                .onSuccess { result ->
                    Timber.e("fetchMissions $result")
                    minLoadingTime.join()
                    _state.value = UiState.Success(
                        KidMissionState(
                            kidMissionByDateList = result.toUiModel(),
                            isRefreshing = false
                        )
                    )
                }
                .onFailure {
                    minLoadingTime.join()
                    _state.updateSuccess { it.copy(isRefreshing = false) }
                    _sideEffect.emit(KidMissionSideEffect.ShowSnackbar(it.message.toString()))
                }
        }
    }

    fun onMissionCompleted(missionId: Long) {
        viewModelScope.launch {
            missionRepository.patchMission(missionId)
                .onSuccess {
                    _state.updateSuccess { currentState ->
                        val updatedGroups =
                            currentState.kidMissionByDateList.missionsByDate.map { group ->
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