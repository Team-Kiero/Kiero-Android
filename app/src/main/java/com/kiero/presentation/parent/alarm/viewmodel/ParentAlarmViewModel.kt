package com.kiero.presentation.parent.alarm.viewmodel

import androidx.lifecycle.SavedStateHandle
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
class ParentAlarmViewModel @Inject constructor(
    private val repository: AlarmRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _localState = MutableStateFlow(LocalState())
    private val childId: Long = savedStateHandle.get<Long>("childId") ?: -1L


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
        if (childId != -1L) {
            loadAlarms()
        } else {
            Timber.e("초기화 실패: childId가 전달되지 않았습니다.")
            _localState.update { it.copy(errorMessage = "자녀 정보를 불러올 수 없습니다.") }
        }
    }

    fun loadAlarms(refresh: Boolean = false) {
        if (childId == -1L) return // 방어 코드

        viewModelScope.launch {
            _localState.update { it.copy(isLoading = true, errorMessage = null) }
            // 멤버 변수 childId 사용
            repository.loadAlarms(childId, refresh = refresh)
                .onSuccess { _localState.update { it.copy(isLoading = false) } }
                .onFailure { error ->
                    _localState.update { it.copy(isLoading = false, errorMessage = error.toHandleErrorMessage()) }
                }
        }
    }

    fun loadMore() {
        if (childId == -1L) return

        val currentState = _localState.value
        if (currentState.isLoadingMore || state.value.nextCursor == null) return

        viewModelScope.launch {
            _localState.update { it.copy(isLoadingMore = true) }
            repository.loadMore(childId)
                .onSuccess { _localState.update { it.copy(isLoadingMore = false) } }
        }
    }

    fun refresh() = loadAlarms(refresh = true)

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