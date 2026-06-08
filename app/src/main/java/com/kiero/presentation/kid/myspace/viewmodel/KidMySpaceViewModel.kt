package com.kiero.presentation.kid.myspace.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.app.AppRestarter
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.data.auth.repository.AuthRepository
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
    private val authRepository: AuthRepository,
    private val userInfoManager: UserInfoManager,
    private val appRestarter: AppRestarter,
    private val fcmRepository: FcmRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(KidMySpaceState())
    val uiState: StateFlow<KidMySpaceState> = _uiState.asStateFlow()

    init {
        fetchKidName()
        fetchPushSetting()
    }

    private fun fetchKidName() {
        viewModelScope.launch {
            coinRepository.getCurrentCoin()
                .onSuccess { coin ->
                    _uiState.update { it.copy(kidName = coin.firstName) }
                }
                .onFailure {
                    Timber.e("fetchKidName fail: $it")
                }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            authRepository.postLogout()
                .onSuccess {
                    userInfoManager.clearKidInfo()
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
                    _uiState.update { it.copy(isNotificationChecked = isEnabled) }
                }
                .onFailure { error ->
                    Timber.e("푸시 알림 설정 조회 실패: $error")
                }
        }
    }

    fun onNotificationToggle(checked: Boolean) {
        _uiState.update { it.copy(isNotificationChecked = checked) }

        viewModelScope.launch {
            fcmRepository.updatePushSetting(checked)
                .onSuccess {
                    Timber.d("푸시 알림 서버 동기화 성공: $checked")
                }
                .onFailure { error ->
                    Timber.e("푸시 알림 설정 변경 실패: $error")
                }
        }
    }

    fun showNotificationDialog(show: Boolean) {
        _uiState.update { it.copy(showNotificationDialog = show) }
    }
}