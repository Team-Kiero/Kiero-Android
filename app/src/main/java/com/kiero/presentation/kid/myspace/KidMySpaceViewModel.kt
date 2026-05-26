package com.kiero.presentation.kid.myspace

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.common.extension.isNotificationEnabled
import com.kiero.data.kid.coin.repository.CoinRepository
import com.kiero.presentation.kid.myspace.state.KidMySpaceState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class KidMySpaceViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val coinRepository: CoinRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(KidMySpaceState())
    val uiState: StateFlow<KidMySpaceState> = _uiState.asStateFlow()

    init {
        syncNotificationState()
        fetchKidName()
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

    fun syncNotificationState() {
        val isEnabled = context.isNotificationEnabled()
        _uiState.update { it.copy(isNotificationChecked = isEnabled) }
    }

    fun onNotificationToggle(checked: Boolean) {
        if (checked && !context.isNotificationEnabled()) {
            _uiState.update { it.copy(showNotificationDialog = true) }
        } else {
            _uiState.update { it.copy(isNotificationChecked = checked) }
        }
    }

    fun onNotificationDialogDismiss() {
        _uiState.update { it.copy(showNotificationDialog = false) }
    }
}