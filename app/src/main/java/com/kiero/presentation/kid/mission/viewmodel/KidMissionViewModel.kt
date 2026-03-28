package com.kiero.presentation.kid.mission.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.common.extension.updateSuccess
import com.kiero.core.common.util.successData
import com.kiero.core.model.UiState
import com.kiero.data.kid.coin.repository.CoinRepository
import com.kiero.data.parent.mission.repository.MissionRepository
import com.kiero.data.sse.manager.SseManager
import com.kiero.presentation.kid.mission.model.toUiModel
import com.kiero.presentation.kid.mission.state.KidMissionSideEffect
import com.kiero.presentation.kid.mission.state.KidMissionState
import com.kiero.presentation.kid.model.toUiModel
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class KidMissionViewModel @Inject constructor(
    private val repository: CoinRepository,
    private val missionRepository: MissionRepository,
    private val sseManager: SseManager
) : ViewModel() {
    private val _state = MutableStateFlow<UiState<KidMissionState>>(UiState.Loading)
    val state: StateFlow<UiState<KidMissionState>> = combine(
        _state,
        repository.myCoin
    ) { uiState, coinData ->
        when (uiState) {
            is UiState.Success -> {
                Timber.e("combine $coinData")
                UiState.Success(
                    uiState.data.copy(
                        coinUiModel = coinData.toUiModel()
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
        sseManager.startChildSubscription()
        collectChildMissionEvents()
    }

    fun collectChildMissionEvents() {
        viewModelScope.launch {
            sseManager.childMissionEvents.collect { event ->
                Timber.e("collectChildMissionEvents $event")
                fetchMissions()
            }
        }
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
                    _state.update { currentState ->
                        val currentData = currentState.successData ?: KidMissionState()
                        UiState.Success(
                            currentData.copy(
                                kidMissionByDateList = result.toUiModel(),
                                isRefreshing = false
                            )
                        )
                    }
                }
                .onFailure {
                    minLoadingTime.join()
                    _state.updateSuccess { it.copy(isRefreshing = false) }
                    _sideEffect.emit(KidMissionSideEffect.ShowSnackbar(it.message.toString()))
                }
        }
    }

    fun onMissionCompleted() {
        viewModelScope.launch {
            val selectedMission = _state.value.successData?.selectedMissionItem ?: return@launch

            missionRepository.patchMission(selectedMission.id)
                .onSuccess {
                    _state.updateSuccess { currentState ->
                        val updatedGroups =
                            currentState.kidMissionByDateList.missionsByDate.map { group ->
                                group.copy(
                                    missions = group.missions.map { mission ->
                                        if (mission.id == selectedMission.id) {
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
                            ),
                            isCompletedMission = true
                        )
                    }
                    fetchCoin()
                }
                .onFailure { exception ->
                    _sideEffect.emit(KidMissionSideEffect.ShowSnackbar(exception.message.toString()))
                }
        }
    }

    fun fetchCoin() {
        viewModelScope.launch {
            repository.getCurrentCoin()
                .onSuccess {
                    Timber.d("fetchCoin: $it")
                }
                .onFailure {
                    Timber.e("fetchCoin fail: $it")
                }
        }
    }

    fun openMissionDialog(targetId: Long) {
        val currentState = state.value.successData ?: return

        val selectedMission = currentState.kidMissionByDateList.missionsByDate
            .flatMap { it.missions }
            .find { it.id == targetId }
            ?: return

        _state.updateSuccess { state ->
            state.copy(
                isVisibleDialog = true,
                isCompletedMission = false,
                selectedMissionItem = selectedMission
            )
        }
    }

    fun dismissDialog() {
        _state.updateSuccess { state ->
            state.copy(
                isVisibleDialog = false,
                isCompletedMission = false
            )
        }
    }
}