package com.kiero.presentation.kid.myspace.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.localstorage.permission.PermissionInfoManager
import com.kiero.core.permission.model.PermissionType
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
    private val permissionInfoManager: PermissionInfoManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(KidMySpaceState())
    val uiState: StateFlow<KidMySpaceState> = _uiState.asStateFlow()

    init {
        fetchKidName()
        observeNotificationDeniedCount()
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


    fun onNotificationToggle(checked: Boolean) {
        // Todo: API 연결 시 서버에 알림 설정 저장
        _uiState.update { it.copy(isNotificationChecked = checked) }
    }

    fun showNotificationDialog(show: Boolean) {
        _uiState.update { it.copy(showNotificationDialog = show) }
    }

    fun onNotificationDialogDismiss() {
        _uiState.update { it.copy(showNotificationDialog = false) }
    }
}