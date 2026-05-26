package com.kiero.presentation.kid.myspace

import android.content.Context
import androidx.lifecycle.ViewModel
import com.kiero.core.common.extension.isNotificationEnabled
import com.kiero.presentation.kid.myspace.state.KidMySpaceState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class KidMySpaceViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(KidMySpaceState())
    val uiState: StateFlow<KidMySpaceState> = _uiState.asStateFlow()

    fun syncNotificationState() {
        val isEnabled = context.isNotificationEnabled()
        _uiState.update {
            it.copy(isNotificationChecked = isEnabled)
        }
    }

    fun onNotificationToggle(checked: Boolean) {
        if (checked && !context.isNotificationEnabled()) {
            _uiState.update {
                it.copy(showNotificationDialog = true)
            }
        } else {
            _uiState.update {
                it.copy(isNotificationChecked = checked)
            }
        }
    }

    fun onNotificationDialogDismiss() {
        _uiState.update {
            it.copy(showNotificationDialog = false)
        }
    }
}