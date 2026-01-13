package com.kiero.presentation.parent.alarm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.presentation.parent.alarm.state.AlarmFeedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmFeedViewModel @Inject constructor(
    // 나중에 Repository 주입
) : ViewModel() {

    private val _state = MutableStateFlow(AlarmFeedState.FAKE)  // FAKE 데이터 사용
    val state = _state.asStateFlow()

    // TODO: 실제 데이터 로딩 로직 구현
    fun loadAlarms(childId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            // API 호출 후 상태 업데이트
        }
    }

    fun toggleExpand(alarmId: String) {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    alarms = currentState.alarms.map { alarm ->
                        if (alarm.id == alarmId) {
                            alarm.copy(isExpanded = !alarm.isExpanded)
                        } else {
                            alarm
                        }
                    }.toPersistentList()
                )
            }
        }
    }
}