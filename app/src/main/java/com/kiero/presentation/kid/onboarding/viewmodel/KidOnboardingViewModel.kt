package com.kiero.presentation.kid.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.localstorage.onboarding.OnboardingManager
import com.kiero.core.model.UiState
import com.kiero.data.kid.coin.repository.CoinRepository
import com.kiero.presentation.kid.onboarding.state.KidOnboardingSideEffect
import com.kiero.presentation.kid.onboarding.state.KidOnboardingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class KidOnboardingViewModel @Inject constructor(
    private val onboardingManager: OnboardingManager,
    private val coinRepository: CoinRepository
) : ViewModel() {
    private val coin = coinRepository.myCoin

    private val _state = MutableStateFlow<UiState<KidOnboardingState>>(UiState.Loading)
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<KidOnboardingSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        fetchCoin()
    }

    fun startJourney() {
        viewModelScope.launch {
            _state.value = UiState.Loading
            onboardingManager.saveIsSawOnboarding(isSaw = true)
            _sideEffect.emit(KidOnboardingSideEffect.NavigateToKid)
        }
    }

    fun fetchCoin() {
        viewModelScope.launch {
            _state.value = UiState.Loading

            coinRepository.getCurrentCoin()
                .onSuccess {
                    Timber.d("fetchCoin: $it")
                    _state.value = UiState.Success(
                        KidOnboardingState(
                            kidName = coin.value.firstName
                        )
                    )
                }
                .onFailure {
                    Timber.e("fetchCoin fail: $it")
                    _state.value = UiState.Failure(it.message.toString())
                }
        }
    }
}