package com.kiero.presentation.parent.screen.reward.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.data.parent.reward.repository.RewardRepository
import com.kiero.presentation.parent.screen.reward.state.ParentRewardSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ParentAddRewardViewModel @Inject constructor(
    private val rewardRepository: RewardRepository,
    private val userInfoManager: UserInfoManager,
) : ViewModel() {
    val nameState = TextFieldState()
    val priceState = TextFieldState("20")

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ParentRewardSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun createReward() {
        val name = nameState.text.toString().trim()
        val price = priceState.text.toString().toIntOrNull() ?: 0

        viewModelScope.launch {
            if (name.isEmpty()) {
                _sideEffect.emit(ParentRewardSideEffect.ShowSnackBar("보상 이름을 입력해주세요."))
                return@launch
            }
            if (price <= 0) {
                _sideEffect.emit(ParentRewardSideEffect.ShowSnackBar("보상을 입력해주세요."))
                return@launch
            }

            _isLoading.value = true
            val childId = userInfoManager.getChildIdInfo() ?: return@launch
            rewardRepository.createCoupon(childId, name, price)
                .onSuccess {
                    _sideEffect.emit(ParentRewardSideEffect.ShowSnackBar("보상이 등록되었습니다."))
                    _sideEffect.emit(ParentRewardSideEffect.NavigateUp)
                }
            _isLoading.value = false
        }
    }

    fun validateAndFixPrice() {
        val text = priceState.text.toString()
        val currentPrice = text.toIntOrNull() ?: 0

        if (currentPrice > 500) {
            priceState.setTextAndPlaceCursorAtEnd("500")
            viewModelScope.launch {
                _sideEffect.emit(ParentRewardSideEffect.ShowSnackBar("최대 보상은 500개입니다"))
            }
        } else if (text.isEmpty() || currentPrice < 1) {
            priceState.setTextAndPlaceCursorAtEnd("1")
        }
    }
}