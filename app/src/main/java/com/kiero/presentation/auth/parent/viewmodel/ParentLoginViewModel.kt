package com.kiero.presentation.auth.parent.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.presentation.auth.model.AuthSideEffect
import com.kiero.presentation.auth.model.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ParentLoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<AuthSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow() // 외부 노출용은 읽기 전용으로
    fun loginWithKakao(context: Context) = viewModelScope.launch {

        Timber.Forest.d("로그인 프로세스 시작")
        // 1. 로딩 시작
        _state.update { it.copy(isLoading = true) }

        authRepository.loginWithKakao(context)
            .onSuccess {
                Timber.Forest.i("로그인 최종 성공")
                _state.update { it.copy(isLoading = false) }
            }.onFailure { throwable ->
                Timber.Forest.e(throwable, "로그인 실패 에러 발생")
                _state.update { it.copy(isLoading = false) }
            }
    }
}