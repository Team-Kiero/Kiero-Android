package com.kiero.presentation.parent.alarm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.common.extension.toHandleErrorMessage
import com.kiero.core.common.util.suspendRunCatching
import com.kiero.core.localstorage.TokenManager
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.data.alarm.repository.AlarmRepository
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.data.demo.repository.DemoRepository
import com.kiero.data.sse.manager.SseManager
import com.kiero.presentation.parent.alarm.model.toUiModel
import com.kiero.presentation.parent.alarm.state.AlarmFeedState
import com.kiero.presentation.signup.parent.state.ParentSignUpSideEffect
import com.kiero.presentation.signup.parent.state.ParentSignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ParentAlarmViewModel @Inject constructor(
    private val repository: AlarmRepository,
    private val userInfoManager: UserInfoManager,
    private val sseManager: SseManager,
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val demoRepository: DemoRepository
) : ViewModel() {

    private val _localState = MutableStateFlow(LocalState())
    private var childId: Long? = null

    private val _authState = MutableStateFlow(ParentSignUpState())
    val authState: StateFlow<ParentSignUpState> = _authState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ParentSignUpSideEffect>()
    val sideEffect: SharedFlow<ParentSignUpSideEffect> = _sideEffect.asSharedFlow()

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
        initFetchParentInfo()
        loadChildIdAndAlarms()
        collectFeedEvents()
    }

    fun initFetchParentInfo() {
        viewModelScope.launch {
            val parentInfo = userInfoManager.getParentInfo()!!

            _authState.update { currentState ->
                currentState.copy(
                    parentInfo = currentState.parentInfo.copy(
                        parentName = parentInfo.name,
                        parentProfileImage = parentInfo.profileImage
                    )
                )
            }
        }
    }

    fun onProfileClick() {
        _authState.update {
            it.copy(isLogoutDialogVisible = true)
        }
    }

    fun onLogoutCancel() {
        _authState.update {
            it.copy(isLogoutDialogVisible = false)
        }
    }

    fun onLogoutConfirm() {
        _authState.update {
            it.copy(
                isLogoutDialogVisible = false,
                isLoading = true
            )
        }

        logOut()
    }

    fun logOut() {
        Timber.e("로그아웃 되었습니다")
        viewModelScope.launch {
            val logoutDeferred = async {
                suspendRunCatching { authRepository.postLogout() }
            }
            val demoDeferred = async {
                suspendRunCatching { demoRepository.deleteDemo() }
            }
            val tokenDeferred = async {
                suspendRunCatching { tokenManager.clearTokens() }
            }

            awaitAll(logoutDeferred, demoDeferred, tokenDeferred)

            _authState.update {
                it.copy(isLoading = false)
            }

            _sideEffect.emit(ParentSignUpSideEffect.NavigateToSelection)
        }
    }

    private fun collectFeedEvents() {
        viewModelScope.launch {
            sseManager.parentFeedEvents.collect { feedEvent ->
                Timber.d("📢 새 알림 도착: ${feedEvent.data.eventType}")
                refresh()
            }
        }
    }

    private fun loadChildIdAndAlarms() {
        viewModelScope.launch {
            childId = userInfoManager.getChildIdInfo()

            if (childId != null) {
                Timber.d("📌 childId 확인: $childId")
                loadAlarms()
            } else {
                Timber.e("📌 childId가 없습니다")
                _localState.update { it.copy(errorMessage = "자녀 정보를 불러올 수 없습니다.") }
            }
        }
    }

    fun loadAlarms(refresh: Boolean = false) {
        val id = childId ?: return

        viewModelScope.launch {
            _localState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.loadAlarms(id, refresh = refresh)
                .onSuccess { _localState.update { it.copy(isLoading = false) } }
                .onFailure { error ->
                    _localState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.toHandleErrorMessage()
                        )
                    }
                }
        }
    }

    fun loadMore() {
        val id = childId ?: return

        val currentState = _localState.value
        if (currentState.isLoadingMore || state.value.nextCursor == null) return

        viewModelScope.launch {
            _localState.update { it.copy(isLoadingMore = true) }
            repository.loadMore(id)
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