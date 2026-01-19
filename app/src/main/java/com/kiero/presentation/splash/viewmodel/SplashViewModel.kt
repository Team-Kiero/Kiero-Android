package com.kiero.presentation.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.localstorage.TokenManager
import com.kiero.core.localstorage.onboarding.OnboardingManager
import com.kiero.core.model.auth.UserRole
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.presentation.splash.state.SplashSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val onboardingManager: OnboardingManager
) : ViewModel() {
    private val _sideEffect = MutableSharedFlow<SplashSideEffect>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val sideEffect = _sideEffect.asSharedFlow()

    init {
        checkLoginState()
    }

    private fun checkLoginState() {
        viewModelScope.launch {
            delay(2000)

            val accessToken = tokenManager.getAccessToken()
            val userRole = tokenManager.getUserRole()

            if (!accessToken.isNullOrBlank() && userRole != null) {
                when (userRole) {
                    UserRole.PARENT -> {
                        authRepository.getChildren()
                            .onSuccess { children ->
                                Timber.e("children $children")
                                if (children.isEmpty()) {
                                    _sideEffect.emit(SplashSideEffect.NavigateToParentGraph)
                                } else {
                                    _sideEffect.emit(SplashSideEffect.NavigateToParentHome)
                                }
                            }
                            .onFailure { t ->
                                Timber.e(t, "자녀 목록 조회 실패")
                                _sideEffect.emit(SplashSideEffect.NavigateToAuth)
                            }
                    }
                    UserRole.KID -> {
                        if (onboardingManager.getIsSawOnboarding()) {
                            _sideEffect.emit(SplashSideEffect.NavigateToKidHome)
                        } else {
                            _sideEffect.emit(SplashSideEffect.NavigateToKidOnboarding)
                        }
                    }
                }
            } else {
                _sideEffect.emit(SplashSideEffect.NavigateToAuth)
            }
        }
    }
}