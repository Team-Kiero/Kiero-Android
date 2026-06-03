package com.kiero.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.app.AppRestarter
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.core.model.auth.UserRole
import com.kiero.data.fcm.repository.FcmRepository
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
    private val fcmRepository: FcmRepository,
    private val sseManager: SseManager,
    private val userInfoManager: UserInfoManager,
    private val appRestarter: AppRestarter,
) : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    init {
        fetchUnreadAlarmStatus()
        syncFcmToken()
        observeSseEventsByRole()
    }
    private fun observeSseEventsByRole() {
        viewModelScope.launch {
            val userRole = userInfoManager.getUserRole() ?: return@launch

            when (userRole) {
                UserRole.PARENT -> observeParentFeedEvent()
                UserRole.KID -> observeParentWithdrawalEvent()
            }
        }
    }

    // 부모용: 피드 이벤트 관찰
    private fun observeParentFeedEvent() {
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

    // 자녀용: 부모 탈퇴 이벤트 관찰
    private fun observeParentWithdrawalEvent() {
        viewModelScope.launch {
            sseManager.childParentWithDrawnEvents.collect {
                Timber.e("부모 탈퇴 감지 -> 자녀 강제 로그아웃 진행")

                sseManager.stopSubscription()
                userInfoManager.clearKidInfo()
                appRestarter.restartApp()
            }
        }
    }

    private fun syncFcmToken() {
        viewModelScope.launch {
            fcmRepository.syncFcmToken()
                .onSuccess {
                    Timber.d("FCM 토큰 동기화(획득 및 서버 갱신) 성공")
                }
                .onFailure {
                    Timber.e(it, "FCM 토큰 동기화 실패")
                }
        }
    }

    fun fetchUnreadAlarmStatus() {
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
        if (!_state.value.unreadAlarm.hasUnread) return
        fetchUnreadAlarmStatus()
    }

    fun resumeSse() {
        viewModelScope.launch {
            sseManager.resumeSse()
        }
    }
}