package com.kiero.presentation.kid.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.localstorage.onboarding.OnboardingManager
import com.kiero.presentation.kid.onboarding.state.KidOnboardingSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KidOnboardingViewModel @Inject constructor(
    private val onboardingManager: OnboardingManager
) : ViewModel() {
    private val _sideEffect = MutableSharedFlow<KidOnboardingSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun startJourney() {
        viewModelScope.launch {
            onboardingManager.saveIsSawOnboarding(isSaw = true)

            _sideEffect.emit(KidOnboardingSideEffect.NavigateToKid)
        }
    }

}