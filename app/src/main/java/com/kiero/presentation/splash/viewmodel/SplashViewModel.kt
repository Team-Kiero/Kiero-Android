package com.kiero.presentation.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.app.AppRestarter
import com.kiero.core.localstorage.TokenManager
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.core.localstorage.onboarding.OnboardingManager
import com.kiero.core.model.auth.UserRole
import com.kiero.core.network.auth.TokenRefreshService
import com.kiero.domain.kid.user.usecase.CheckParentStatusUseCase
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
    private val userInfoManager: UserInfoManager,
    private val onboardingManager: OnboardingManager,
    private val reIssueManager: TokenRefreshService,
    private val checkParentStatusUseCase: CheckParentStatusUseCase,
    private val checkParentAutoLoginUseCase: CheckParentAutoLoginUseCase,
    private val appRestarter: AppRestarter,
) : ViewModel() {
    private val _sideEffect = Channel<SplashSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    fun checkLoginState() {
        viewModelScope.launch {
            delay(2000)

            val accessToken = tokenManager.getAccessToken()
            val userRole = userInfoManager.getUserRole()
            val refreshToken = tokenManager.getRefreshToken()
            val isRequiredTermsAllAgreed = userInfoManager.getTermsInfo() ?: false

            val isParentAndAgreed = userRole == UserRole.PARENT && isRequiredTermsAllAgreed
            val isKid = userRole == UserRole.KID

            // 토큰이 존재하면서 (부모+동의완료) 이거나 (자녀)인 경우
            if (!accessToken.isNullOrBlank() && userRole != null && !refreshToken.isNullOrBlank() && (isParentAndAgreed || isKid)) {
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
                // 토큰이 없거나 부모인데 약관 동의를 안 한 경우 인증 화면으로 이동
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
        // 부모의 탈퇴 여부를 먼저 확인
        val isParentWithdrawn = checkParentStatusUseCase().getOrNull() ?: false

        if (isParentWithdrawn) {
            userInfoManager.clearKidInfo()
            appRestarter.restartApp()
            return
        }

        // 부모가 탈퇴하지 않은 정상 상태일 때만 기존 온보딩/홈 진입 로직 수행
        if (onboardingManager.getIsSawOnboarding()) {
            _sideEffect.send(SplashSideEffect.NavigateToKidHome)
        } else {
            _sideEffect.send(SplashSideEffect.NavigateToKidOnboarding)
        }
    }
}