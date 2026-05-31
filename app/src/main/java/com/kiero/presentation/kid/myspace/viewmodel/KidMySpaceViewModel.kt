package com.kiero.presentation.kid.myspace.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.localstorage.permission.PermissionInfoManager
import com.kiero.core.permission.model.PermissionType
import com.kiero.data.fcm.repository.FcmRepository
import com.kiero.data.kid.coin.repository.CoinRepository
import com.kiero.presentation.kid.myspace.state.KidMySpaceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class KidMySpaceViewModel @Inject constructor(
    private val coinRepository: CoinRepository,
    private val permissionInfoManager: PermissionInfoManager,
    private val fcmRepository: FcmRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(KidMySpaceState())
    val uiState: StateFlow<KidMySpaceState> = _uiState.asStateFlow()

    init {
        fetchKidName()
        observeNotificationDeniedCount()
        fetchPushSetting()
    }

    private fun fetchKidName() {
        viewModelScope.launch {
            coinRepository.getCurrentCoin()
                .onSuccess { coin ->
                    _uiState.update { it.copy(kidName = coin.firstName) }
                }
                .onFailure {
                    Timber.Forest.e("fetchKidName fail: $it")
                }
        }
    }

    private fun observeNotificationDeniedCount() {
        viewModelScope.launch {
            permissionInfoManager.deniedCount(PermissionType.POST_NOTIFICATIONS).collect { count ->
                _uiState.update { it.copy(permissionNotificationDeniedCount = count) }
            }
        }
    }

    fun increaseDeniedCount(type: PermissionType) {
        viewModelScope.launch {
            permissionInfoManager.increaseDeniedCount(type)
        }
    }

    private fun fetchPushSetting() {
        viewModelScope.launch {
            fcmRepository.getPushSetting()
                .onSuccess { isEnabled ->
                    _uiState.update { it.copy(isNotificationChecked = isEnabled) }
                }
                .onFailure { error ->
                    Timber.e("푸시 알림 설정 조회 실패: $error")
                }
        }
    }

   fun onNotificationToggle(checked: Boolean) {
        viewModelScope.launch {
            fcmRepository.updatePushSetting(checked)
                .onSuccess {
                   _uiState.update { it.copy(isNotificationChecked = checked) }
                }
                .onFailure { error ->
                    Timber.e("푸시 알림 설정 변경 실패: $error")
                }
        }
    }

    fun showNotificationDialog(show: Boolean) {
        _uiState.update { it.copy(showNotificationDialog = show) }
    }

    fun onNotificationDialogDismiss() {
        _uiState.update { it.copy(showNotificationDialog = false) }
    }
}