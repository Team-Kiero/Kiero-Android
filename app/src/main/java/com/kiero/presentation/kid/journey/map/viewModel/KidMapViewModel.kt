package com.kiero.presentation.kid.journey.map.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kiero.core.common.extension.updateSuccess
import com.kiero.core.model.UiState
import com.kiero.data.kid.schedule.repository.ScheduleRepository
import com.kiero.data.sse.manager.SseManager
import com.kiero.presentation.kid.journey.map.model.KidMapScheduleStatus
import com.kiero.presentation.kid.journey.map.model.toUiModel
import com.kiero.presentation.kid.journey.map.model.toUiModelList
import com.kiero.presentation.kid.journey.map.navigation.Map
import com.kiero.presentation.kid.journey.map.state.KidMapState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class KidMapViewModel @Inject constructor(
    private val repository: ScheduleRepository,
    private val sseManager: SseManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val map = savedStateHandle.toRoute<Map>()

    private val _state = MutableStateFlow<UiState<KidMapState>>(UiState.Loading)
    val state = _state.asStateFlow()

    init {
        fetchScheduleProgress()

        sseManager.startChildSubscription()
        collectChildKidScheduleEvents()
    }

    private fun fetchScheduleProgress() {
        viewModelScope.launch {
            repository.getScheduleProgress()
                .onSuccess { scheduleData ->
                    _state.value = UiState.Success(
                        KidMapState(
                            date = map.date,
                            scheduleCount = scheduleData.scheduleCount,
                            schedules = scheduleData.schedules.toUiModelList().toPersistentList()
                        )
                    )
                    Timber.d("fetchScheduleProgress success: $scheduleData")
                }
                .onFailure {
                    Timber.e("fetchScheduleProgress failed: $it")
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
                    fetchScheduleProgress()
                }
            }
        }
    }
}