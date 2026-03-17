package com.kiero.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.data.parent.alarm.repository.AlarmRepository
import com.kiero.data.sse.manager.SseManager
import com.kiero.presentation.main.model.toUiModel
import com.kiero.presentation.main.state.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val sseManager: SseManager
) : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    init {
        fetchUnreadAlarmStatus()
        observeFeedEvent()
    }

    private fun fetchUnreadAlarmStatus() {
        viewModelScope.launch {
            alarmRepository.getUnreadAlarm()
                .onSuccess { result ->
                    Timber.e("fetchUnreadAlarmStatus $result")
                    _state.update {
                        it.copy(unreadAlarm = result.toUiModel())
                    }
                }
                .onFailure(Timber::e)
        }
    }

    private fun observeFeedEvent() {
        viewModelScope.launch {
            sseManager.parentFeedEvents.collect {
                Timber.d("sseManager.parentFeedEvents: $it")
                _state.update { currentState ->
                    currentState.copy(
                        unreadAlarm = currentState.unreadAlarm.copy(
                            hasUnread = true
                        )
                    )
                }
            }
        }
    }

    fun onAlarmRead() {
        _state.update {
            it.copy(
                unreadAlarm = it.unreadAlarm.copy(
                    hasUnread = false,
                    unreadChildIds = emptyList()
                )
            )
        }
    }
}