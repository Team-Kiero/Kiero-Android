package com.kiero.presentation.parent.screen.journey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.data.parent.journey.repository.ParentJourneyRepository
import com.kiero.data.sse.manager.SseManager
import com.kiero.data.sse.model.parent.SseScheduleEventType
import com.kiero.presentation.parent.screen.journey.model.TodayJourneyUiModel
import com.kiero.presentation.parent.screen.journey.model.TodayStatus
import com.kiero.presentation.parent.screen.journey.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class ParentJourneyViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val parentJourneyRepository: ParentJourneyRepository,
    private val sseManager: SseManager
) : ViewModel() {
    private val _state = MutableStateFlow(ParentJourneyState())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ParentJourneySideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        fetchKidInfo()
        sseManager.startParentSubscription()
    }

    fun collectConnectionEvents() {
        viewModelScope.launch {
            sseManager.connectionState.collect { isConnected ->
                if (isConnected) {
                    val childId = _state.value.kidInfo.kidId.toLong()

                    fetchParentJourney(childId)
                }
            }
        }
    }

    fun collectParentJourneyScheduleEvents() {
        viewModelScope.launch {
            sseManager.parentScheduleEvents.collect { event ->
                val childId = _state.value.kidInfo.kidId.toLong()

                when (event.data.scheduleEventType) {
                    SseScheduleEventType.SCHEDULE_STATUS_UPDATED, SseScheduleEventType.TODAY_MISSION_COMPLETED -> {
                        fetchParentJourney(childId)
                    }
                    SseScheduleEventType.FIRE_LIT -> {
                        _state.update { it.copy(isFireLitToday = true) }
                        fetchParentJourney(childId)
                    }
                    SseScheduleEventType.UNKNOWN -> {
                        Timber.w("알 수 없는 schedule 이벤트: ${event.data.eventType}")
                    }
                }
            }
        }
    }

    private fun fetchKidInfo() {
        viewModelScope.launch {
            authRepository.getChildren()
                .onSuccess { response ->
                    val kidInfo = response.firstOrNull()?.toUiModel()
                    Timber.e("fetchKidInfo, $kidInfo")
                    if (kidInfo != null) {
                        _state.update { currentState ->
                            currentState.copy(
                                kidInfo = kidInfo
                            )
                        }

                        collectParentJourneyScheduleEvents()
                        collectConnectionEvents()
                        fetchParentJourney(kidInfo.kidId.toLong())
                    }
                }
                .onFailure {
                    Timber.e("parentJourney ${it.message.toString()}")
                    _sideEffect.emit(ParentJourneySideEffect.ShowSnackbar(message = "아이 정보 불러오기에 실패하였습니다"))
                }
        }
    }

    fun fetchParentJourney(childId: Long) {
        viewModelScope.launch {
            parentJourneyRepository.getParentJourney(
                childId = childId
            ).onSuccess { result ->
                val currentTime = LocalTime.now()

                _state.update { currentState ->
                    currentState.copy(
                        isFireLitToday = result.isFireLitToday,
                        completeMissions = result.completeMissions.map { it.toUiModel() }.toImmutableList(),
                        incompleteMissions = result.incompleteMissions.map { it.toUiModel() }.toImmutableList(),
                        todayMissionList = if (result.isFireLitToday) {
                            result.schedules.toUiModels(currentTime).toPersistentList().add(
                                TodayJourneyUiModel(todayStatus = TodayStatus.TODAY_COMPLETED)
                            )
                        } else {
                            result.schedules.toUiModels(currentTime).toPersistentList()
                        }
                    )
                }
            }.onFailure {
                Timber.e("parentJourney ${it.message.toString()}")
                _sideEffect.emit(ParentJourneySideEffect.ShowSnackbar(message = "일정 정보 불러오기에 실패하였습니다"))
            }
        }
    }

    fun fetchScheduleImage(
        scheduleDetailId: Long
    ) {
        viewModelScope.launch {
            parentJourneyRepository.patchScheduleImage(
                scheduleDetailId = scheduleDetailId
            ).onSuccess { result ->
                _state.update {
                    it.copy(
                        selectedJourneyImageUrl = result.imageUrl
                    )
                }
            }.onFailure {
                Timber.e(it)
                _sideEffect.emit(ParentJourneySideEffect.ShowSnackbar(message = "이미지 정보 불러오기에 실패하였습니다"))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        sseManager.stopSubscription()
    }
}
