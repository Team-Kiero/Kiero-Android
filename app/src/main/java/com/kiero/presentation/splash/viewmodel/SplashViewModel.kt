package com.kiero.presentation.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.localstorage.TokenManager
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.core.localstorage.onboarding.OnboardingManager
import com.kiero.core.model.auth.UserRole
import com.kiero.core.network.auth.TokenRefreshService
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.domain.parent.splash.model.ParentAutoLoginResult
import com.kiero.domain.parent.splash.usecase.CheckParentAutoLoginUseCase
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
    private val tokenManager: TokenManager,
    private val onboardingManager: OnboardingManager,
    private val reIssueManager: TokenRefreshService,
    private val checkParentAutoLoginUseCase: CheckParentAutoLoginUseCase
) : ViewModel() {
    private val _sideEffect = Channel<SplashSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    fun checkLoginState() {
        viewModelScope.launch {
            delay(2000)

            val accessToken = tokenManager.getAccessToken()
            val userRole = tokenManager.getUserRole()
            val refreshToken = tokenManager.getRefreshToken()

            if (!accessToken.isNullOrBlank() && userRole != null && !refreshToken.isNullOrBlank()) {
                reIssueManager.refresh(
                    refreshToken = refreshToken,
                    role = userRole
                ).onSuccess {
                    when (userRole) {
                        UserRole.PARENT -> handleParentLogin()
                        UserRole.KID -> handleKidLogin()
                    }
                }.onFailure { t ->
                    Timber.e(t, "토큰 갱신 실패 - 재로그인 필요")
                    _sideEffect.send(SplashSideEffect.NavigateToAuth)
                }
            } else {
                _sideEffect.send(SplashSideEffect.NavigateToAuth)
            }
        }
    }

    private suspend fun handleParentLogin() {
        val result = checkParentAutoLoginUseCase()

        when (result) {
            /*is ParentAutoLoginResult.MoveToTermsAgreement ->
                _sideEffect.send(SplashSideEffect.NavigateToAuth)*/

            is ParentAutoLoginResult.MoveToParentHome ->
                _sideEffect.send(SplashSideEffect.NavigateToParentHome) // 자녀가 있으면 첫 번째 자녀 정보 저장 후 스케줄로 NavigateToParentHome

            is ParentAutoLoginResult.MoveToOnboarding ->
                _sideEffect.send(SplashSideEffect.NavigateToParentGraph) // 부모 카카오 로그인 화면

            is ParentAutoLoginResult.MoveToAuth ->
                _sideEffect.send(SplashSideEffect.NavigateToAuth) // 역할 선택 화면
        }
    }

    // 아이 로그인 처리 분리
    private suspend fun handleKidLogin() {
        if (onboardingManager.getIsSawOnboarding()) {
            // 온보딩을 봤다면
            _sideEffect.send(SplashSideEffect.NavigateToKidHome)
        } else {
            _sideEffect.send(SplashSideEffect.NavigateToKidOnboarding)
        }
    }
}