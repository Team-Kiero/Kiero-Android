package com.kiero.presentation.kid.journey.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.common.util.successData
import com.kiero.core.localstorage.permission.PermissionInfoManager
import com.kiero.core.model.UiState
import com.kiero.core.permission.model.PermissionType
import com.kiero.data.kid.coin.repository.CoinRepository
import com.kiero.data.kid.schedule.model.ScheduleStatus
import com.kiero.data.kid.schedule.repository.ScheduleRepository
import com.kiero.data.sse.manager.SseManager
import com.kiero.presentation.kid.journey.model.KidJourneyContentUiModel
import com.kiero.presentation.kid.journey.model.KidJourneyHeaderUiModel
import com.kiero.presentation.kid.journey.model.KidJourneyScheduleUiModel
import com.kiero.presentation.kid.journey.model.KidJourneyStoneType
import com.kiero.presentation.kid.journey.state.KidJourneySideEffect
import com.kiero.presentation.kid.journey.state.KidJourneyState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class KidJourneyViewModel @Inject constructor(
    private val repository: ScheduleRepository,
    private val coinRepository: CoinRepository,
    private val sseManager: SseManager,
    private val permissionInfoManager: PermissionInfoManager
) : ViewModel() {
    private val coin = coinRepository.myCoin

    private val _state = MutableStateFlow<UiState<KidJourneyState>>(UiState.Loading)
    val state: StateFlow<UiState<KidJourneyState>> = combine(
        _state,
        coinRepository.myCoin
    ) { uiState, coinData ->
        when (uiState) {
            is UiState.Success -> {
                val currentHeader = uiState.data.header
                UiState.Success(
                    uiState.data.copy(
                        header = currentHeader?.copy(
                            kidName = coinData.firstName,
                            currentDate = coinData.today,
                            coinCount = coinData.coinAmount
                        ) ?: KidJourneyHeaderUiModel(
                            kidName = coinData.firstName,
                            currentDate = coinData.today,
                            coinCount = coinData.coinAmount,
                            earnedStones = 0,
                            totalScheduleCount = 0
                        )
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

    private val _sideEffect = MutableSharedFlow<KidJourneySideEffect>()
    val sideEffect: SharedFlow<KidJourneySideEffect> = _sideEffect.asSharedFlow()

    init {
        sseManager.startChildSubscription()
        collectChildKidScheduleEvents()
        observeNotificationDeniedCount()
    }

    fun fetchData() {
        fetchTodaySchedule()
        fetchCoin()
    }

    fun checkNotificationPermission(isAlreadyGranted: Boolean) {
        if (isAlreadyGranted) return

        viewModelScope.launch {
            val deniedCount = permissionInfoManager.deniedCount(PermissionType.POST_NOTIFICATIONS).first()
            if (deniedCount == 0) {
                _state.update { uiState ->
                    if (uiState is UiState.Success) {
                        UiState.Success(uiState.data.copy(showNotificationPermissionDialog = true))
                    } else uiState
                }
            }
        }
    }

    fun onNotificationPermissionDialogDismiss() {
        viewModelScope.launch {
            permissionInfoManager.increaseDeniedCount(PermissionType.POST_NOTIFICATIONS)
            _state.update { uiState ->
                if (uiState is UiState.Success) {
                    UiState.Success(uiState.data.copy(showNotificationPermissionDialog = false))
                } else uiState
            }
        }
    }

    private fun observeNotificationDeniedCount() {
        viewModelScope.launch {
            permissionInfoManager.deniedCount(PermissionType.POST_NOTIFICATIONS).collect { count ->
                _state.update { uiState ->
                    if (uiState is UiState.Success) {
                        UiState.Success(uiState.data.copy(permissionNotificationDeniedCount = count))
                    } else uiState
                }
            }
        }
    }

    private fun collectChildKidScheduleEvents() {
        viewModelScope.launch {
            val scheduleFlow = sseManager.childScheduleEvents.map { it.data.eventType }
            val dateFlow = sseManager.dateEvents.map { it.data.eventType }

            merge(scheduleFlow, dateFlow).collect { eventType ->
                Timber.e("이벤트 수신: $eventType")

                if (eventType == "SCHEDULE_STATUS_UPDATED" ||
                    eventType == "SCHEDULE_MODIFIED" ||
                    eventType == "DATE_CHANGED"
                ) {
                    fetchData()
                }
            }
        }
    }

    private fun fetchTodaySchedule() {
        viewModelScope.launch {
            repository.patchScheduleToday()
                .onSuccess { scheduleData ->
                    val currentDeniedCount = (_state.value as? UiState.Success)?.data?.permissionNotificationDeniedCount ?: 0
                    val stoneType = scheduleData.stoneType?.let { KidJourneyStoneType.from(it) }
                    val scheduleInfo = KidJourneyScheduleUiModel(
                        order = scheduleData.scheduleOrder,
                        startTime = scheduleData.startTime,
                        endTime = scheduleData.endTime
                    )

                    _state.value = UiState.Success(
                        KidJourneyState(
                            permissionNotificationDeniedCount = currentDeniedCount,
                            header = KidJourneyHeaderUiModel(
                                kidName = coin.value.firstName,
                                currentDate = coin.value.today,
                                coinCount = coin.value.coinAmount,
                                earnedStones = scheduleData.earnedStones,
                                totalScheduleCount = scheduleData.totalSchedule
                            ),
                            content = when (scheduleData.scheduleStatus) {
                                ScheduleStatus.NO_SCHEDULE -> KidJourneyContentUiModel.NoSchedule

                                ScheduleStatus.FIRST_SCHEDULE -> KidJourneyContentUiModel.FirstSchedule(
                                    scheduleDetailId = scheduleData.scheduleDetailId,
                                    scheduleName = scheduleData.name,
                                    stoneType = stoneType,
                                    scheduleInfo = scheduleInfo,
                                    isSkippable = scheduleData.isSkippable,
                                    isNowScheduleVerified = scheduleData.isNowScheduleVerified
                                )

                                ScheduleStatus.NOW_SCHEDULE_EXIST -> KidJourneyContentUiModel.NowSchedule(
                                    scheduleDetailId = scheduleData.scheduleDetailId,
                                    scheduleName = scheduleData.name,
                                    stoneType = stoneType,
                                    scheduleInfo = scheduleInfo,
                                    isSkippable = scheduleData.isSkippable,
                                    isNowScheduleVerified = scheduleData.isNowScheduleVerified
                                )

                                ScheduleStatus.NEXT_SCHEDULE_EXIST -> KidJourneyContentUiModel.NextSchedule(
                                    scheduleDetailId = scheduleData.scheduleDetailId,
                                    scheduleName = scheduleData.name,
                                    stoneType = stoneType,
                                    scheduleInfo = scheduleInfo,
                                    isSkippable = scheduleData.isSkippable,
                                    isNowScheduleVerified = scheduleData.isNowScheduleVerified
                                )

                                ScheduleStatus.FIRE_NOT_LIT -> KidJourneyContentUiModel.FireNotLit(
                                    kidName = coin.value.firstName
                                )

                                ScheduleStatus.FIRE_LIT -> KidJourneyContentUiModel.FireLit
                            }
                        )
                    )
                    Timber.d("fetchTodaySchedule: $scheduleData")
                }
                .onFailure {
                    Timber.e("fetchTodaySchedule fail: $it")
                }
        }
    }

    fun onNextClick() {
        val content = _state.value.successData?.content
        val scheduleDetailId =
            (content as? KidJourneyContentUiModel.ScheduledContent)?.scheduleDetailId

        if (scheduleDetailId != null) {
            viewModelScope.launch {
                _state.value = UiState.Loading

                repository.patchScheduleSkip(scheduleDetailId)
                    .onSuccess {
                        fetchTodaySchedule()
                        Timber.d("patchScheduleSkip: $it")
                    }
                    .onFailure {
                        Timber.d("patchScheduleSkip: $it")
                    }
            }
        }
    }

    private fun fetchCoin() {
        viewModelScope.launch {
            coinRepository.getCurrentCoin()
                .onSuccess {
                    Timber.d("fetchCoin: $it")
                }
                .onFailure {
                    Timber.e("fetchCoin fail: $it")
                }
        }
    }

    fun increaseDeniedCount(type: PermissionType) {
        viewModelScope.launch {
            permissionInfoManager.increaseDeniedCount(type)
        }
    }
}