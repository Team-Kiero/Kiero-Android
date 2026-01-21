package com.kiero.presentation.auth.parent.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.common.extension.toHandleErrorMessage
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.core.model.UiState
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.presentation.auth.state.AuthSideEffect
import com.kiero.presentation.auth.state.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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
class AuthParentViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userInfoManager: UserInfoManager,
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<AuthSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun loginWithKakao(context: Context) = viewModelScope.launch {
        _state.update { it.copy(uiState = UiState.Loading) }

        authRepository.loginWithKakao(context)
            .onSuccess { result ->
                userInfoManager.saveParentInfo(
                    parentName = result.name,
                    parentProfileImage = result.image
                )

                val childrenDeferred = async { authRepository.getChildren() }
                val childrenResult = childrenDeferred.await()

                childrenResult.onSuccess { children ->
                    // 먼저 체크, 그 다음 저장
                    if (children.isNotEmpty()) {
                        userInfoManager.saveChildIdInfo(
                            childId = children.first().childId
                        )
                        _sideEffect.emit(
                            AuthSideEffect.NavigateToParentGraph
                        )
                    } else {
                        _sideEffect.emit(
                            AuthSideEffect.NavigateToParentSignUp(
                                parentName = result.name,
                                parentProfileImage = result.image
                            )
                        )
                    }
                }.onFailure { throwable ->
                    Timber.e(throwable)
                    _sideEffect.emit(
                        AuthSideEffect.ShowSnackbar(
                            message = throwable.toHandleErrorMessage()
                        )
                    )
                    _state.update { currentState ->
                        currentState.copy(
                            uiState = UiState.Failure(throwable.toHandleErrorMessage())
                        )
                    }
                }

            }.onFailure { throwable ->
                Timber.e(throwable)
                _state.update {
                    it.copy(uiState = UiState.Failure(throwable.toHandleErrorMessage()))
                }

                _sideEffect.emit(
                    AuthSideEffect.ShowSnackbar(
                        message = throwable.toHandleErrorMessage()
                    )
                )
            }
    }

    fun navigateUp() {
        viewModelScope.launch {
            _sideEffect.emit(AuthSideEffect.NavigateUp)
        }
    }
}