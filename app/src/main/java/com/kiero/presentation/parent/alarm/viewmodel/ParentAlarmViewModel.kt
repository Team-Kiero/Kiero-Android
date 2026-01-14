package com.kiero.presentation.parent.alarm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.common.extension.toHandleErrorMessage
import com.kiero.data.alarm.repository.AlarmRepository
import com.kiero.presentation.parent.alarm.model.toUiModel
import com.kiero.presentation.parent.alarm.state.AlarmFeedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AlarmFeedViewModel @Inject constructor(
    private val repository: AlarmRepository
) : ViewModel() {

    private val _localState = MutableStateFlow(LocalState())

    val state: StateFlow<AlarmFeedState> = combine(
        _localState,
        repository.cachedAlarms,
        repository.childName,
        repository.nextCursor
    ) { localState, alarms, childName, cursor ->
        AlarmFeedState(
            isLoading = localState.isLoading,
            alarms = alarms.map { it.toUiModel(childName) }.map { uiModel ->
                val localExpanded = localState.expandedIds.contains(uiModel.id)
                uiModel.copy(isExpanded = localExpanded)
            }.toPersistentList(),
            errorMessage = localState.errorMessage,
            isLoadingMore = localState.isLoadingMore,
            hasMore = cursor != null,
            nextCursor = cursor
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AlarmFeedState()
    )

    init {
        // TODO: 실제 유저 정보 연동 시 childId 동적 주입 (현재 테스트용 1)
        loadAlarms(childId = 1)

        // TODO: SSE 실시간 구독 로직 및 토큰 처리 완성 후 활성화 예정
    }

    fun loadAlarms(childId: Long, refresh: Boolean = false) {
        viewModelScope.launch {
            _localState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.loadAlarms(childId, refresh = refresh)
                .onSuccess { _localState.update { it.copy(isLoading = false) } }
                .onFailure { error ->
                    _localState.update { it.copy(isLoading = false, errorMessage = error.toHandleErrorMessage()) }
                }
        }
    }

    fun loadMore(childId: Long) {
        val currentState = _localState.value
        if (currentState.isLoadingMore || state.value.nextCursor == null) return
        viewModelScope.launch {
            _localState.update { it.copy(isLoadingMore = true) }
            repository.loadMore(childId)
                .onSuccess { _localState.update { it.copy(isLoadingMore = false) } }
                .onFailure { /* TODO: 추가 에러 처리 */ }
        }
    }

    fun refresh(childId: Long) = loadAlarms(childId, refresh = true)

    fun toggleExpand(alarmId: String) {
        _localState.update { currentState ->
            val newExpandedIds = if (currentState.expandedIds.contains(alarmId)) {
                currentState.expandedIds - alarmId
            } else {
                currentState.expandedIds + alarmId
            }
            currentState.copy(expandedIds = newExpandedIds)
        }
    }

    private data class LocalState(
        val isLoading: Boolean = false,
        val isLoadingMore: Boolean = false,
        val errorMessage: String? = null,
        val expandedIds: Set<String> = emptySet()
    )
}