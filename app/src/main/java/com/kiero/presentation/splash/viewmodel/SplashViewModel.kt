package com.kiero.presentation.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.localstorage.TokenManager
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.core.localstorage.onboarding.OnboardingManager
import com.kiero.core.model.auth.UserRole
import com.kiero.core.network.auth.TokenRefreshService
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.presentation.splash.state.SplashSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val onboardingManager: OnboardingManager,
    private val userInfoManager: UserInfoManager,
    private val reIssueManager: TokenRefreshService,
) : ViewModel() {
    private val _sideEffect = Channel<SplashSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()


    fun checkLoginState() {
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
                                    _sideEffect.send(SplashSideEffect.NavigateToParentGraph)
                                } else {
                                    // Todo : 추후 스프린트 시 수정 지금은 first
                                    Timber.e("children.first().childId ${children.first().childId}")
                                    delay(1000L)
                                    userInfoManager.saveChildIdInfo(children.first().childId)

                                    _sideEffect.send(SplashSideEffect.NavigateToParentHome)
                                }
                            }
                            .onFailure { t ->
                                Timber.e(t, "자녀 목록 조회 실패")
                                _sideEffect.send(SplashSideEffect.NavigateToAuth)
                            }
                    }
                    UserRole.KID -> {
                        reIssueManager.refresh(
                            refreshToken = tokenManager.getRefreshToken().orEmpty(),
                            role = UserRole.KID
                        ).onSuccess {
                            if (onboardingManager.getIsSawOnboarding()) {
                                _sideEffect.send(SplashSideEffect.NavigateToKidHome)
                            } else {
                                _sideEffect.send(SplashSideEffect.NavigateToKidOnboarding)
                            }
                        }.onFailure { t ->
                            Timber.e(t, "refreshn조회 실패")
                            _sideEffect.send(SplashSideEffect.NavigateToAuth)
                        }
                    }
                }
            } else {
                _sideEffect.send(SplashSideEffect.NavigateToAuth)
            }
        }
    }
}