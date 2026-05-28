package com.kiero.presentation.parent.screen.mypage.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.app.AppRestarter
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.core.localstorage.permission.PermissionInfoManager
import com.kiero.core.model.parent.ParentInfo
import com.kiero.core.permission.model.PermissionType
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.data.fcm.repository.FcmRepository
import com.kiero.presentation.parent.screen.mypage.model.ChildConnectionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ParentMyPageViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userInfoManager: UserInfoManager,
    private val appRestarter: AppRestarter,
    private val fcmRepository: FcmRepository,
    private val permissionInfoManager: PermissionInfoManager
) : ViewModel() {
    private val _state = MutableStateFlow(ParentMyPageState())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ParentMyPageSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    val notificationDeniedCount = permissionInfoManager.deniedCount(PermissionType.POST_NOTIFICATIONS)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )


    init {
        fetchInfo()
        fetchPushSetting()
    }

    fun fetchInfo() {
        viewModelScope.launch {
            val kidInfo = userInfoManager.getChildIdInfo()
            val userInfo = userInfoManager.getParentInfo()

            _state.update {
                it.copy(
                    parentInfo = userInfo ?: ParentInfo(),
                    connectionStatus = if (kidInfo != null) ChildConnectionStatus.CONNECTED else ChildConnectionStatus.PENDING
                )
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            authRepository.postLogout()
                .onSuccess {
                    userInfoManager.clearParentInfo()
                    appRestarter.restartApp()
                }
                .onFailure {
                    Timber.e("Logout failed $it")
                }
        }
    }

    private fun fetchPushSetting() {
        viewModelScope.launch {
            fcmRepository.getPushSetting()
                .onSuccess { isEnabled ->
                    _state.update { it.copy(isAlarmChecked = isEnabled) }
                }
                .onFailure { error ->
                    Timber.e("푸시 알림 설정 조회 실패: $error")
                }
        }
    }

    fun increasePermissionDeniedCount(type: PermissionType) {
        viewModelScope.launch {
            permissionInfoManager.increaseDeniedCount(type)
        }
    }

    fun updateIsAlarmChecked(isEnabled: Boolean) {
        viewModelScope.launch {
            fcmRepository.updatePushSetting(isEnabled)
                .onSuccess {
                    _state.update { it.copy(isAlarmChecked = isEnabled) }
                    _sideEffect.emit(ParentMyPageSideEffect.ShowToast("알림 설정이 변경되었습니다."))
                }
                .onFailure { error ->
                    Timber.e("푸시 알림 설정 변경 실패: $error")
                    _sideEffect.emit(ParentMyPageSideEffect.ShowSnackBar("알림 설정 변경에 실패했습니다."))
                }
        }
    }

    // 아이의 연결 유무를 판단하여 ui 업데이트 및 화면 이동
    // Todo : 아이의 연결 여부 처리 확인하기
    fun checkChildCare() {
        viewModelScope.launch {
            _sideEffect.emit(ParentMyPageSideEffect.NavigateToChildCare)
        }
    }
}