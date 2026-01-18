package com.kiero.presentation.auth.kid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class KidSignupViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(KidState())
    val state: StateFlow<KidState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<KidSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun onSignupClick(
        firstName: String,
        lastName: String,
        inviteCode: String,
    ) {
        viewModelScope.launch {
            when {
                firstName.isBlank() && lastName.isBlank() && inviteCode.isBlank() -> {
                    _sideEffect.emit(KidSideEffect.ShowSnackBar("이름이나 초대 코드를 확인해줘."))
                }

                else -> {
                    // TODO : 테스트 후 고치기.
                }
            }
        }
    }
}