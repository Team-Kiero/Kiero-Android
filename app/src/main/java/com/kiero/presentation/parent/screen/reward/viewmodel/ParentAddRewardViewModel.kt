package com.kiero.presentation.parent.screen.reward.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.data.parent.reward.repository.RewardRepository
import com.kiero.presentation.parent.screen.reward.model.RewardPriceDefaults
import com.kiero.presentation.parent.screen.reward.state.ParentRewardFormState
import com.kiero.presentation.parent.screen.reward.state.ParentRewardSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParentAddRewardViewModel @Inject constructor(
    private val rewardRepository: RewardRepository,
    private val userInfoManager: UserInfoManager,
) : ViewModel() {
    val nameState = TextFieldState()
    val priceState = TextFieldState(RewardPriceDefaults.DEFAULT_PRICE.toString())

    private val _state = MutableStateFlow(ParentRewardFormState())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ParentRewardSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        viewModelScope.launch {
            snapshotFlow { priceState.text.toString() }.collectLatest{ text ->
                val num = text.toIntOrNull()
                if (num != null) {
                    if (num > RewardPriceDefaults.MAX_PRICE) {
                        priceState.edit { replace(0, length, RewardPriceDefaults.MAX_PRICE.toString()) }
                        _sideEffect.emit(ParentRewardSideEffect.ShowSnackBar("최대 보상은 ${RewardPriceDefaults.MAX_PRICE}개입니다"))
                    } else if (num == 0) {
                        priceState.edit { replace(0, length, RewardPriceDefaults.MIN_PRICE.toString()) }
                    }
                }
            }
        }
    }

    fun createReward() {
        validateAndFixPrice()
        val name = nameState.text.toString().trim()
        val price = priceState.text.toString().toIntOrNull() ?: 0

        viewModelScope.launch {
            if (name.isEmpty()) {
                _sideEffect.emit(ParentRewardSideEffect.ShowSnackBar("보상 이름을 입력해주세요."))
                return@launch
            }
            if (price < RewardPriceDefaults.MIN_PRICE) {
                _sideEffect.emit(ParentRewardSideEffect.ShowSnackBar("보상을 올바르게 입력해주세요."))
                return@launch
            }

            _state.update { it.copy(isLoading = true) }
            val childId = userInfoManager.getChildIdInfo() ?: return@launch
            rewardRepository.createCoupon(childId, name, price)
                .onSuccess {
                    _sideEffect.emit(ParentRewardSideEffect.ShowSnackBar("보상이 등록되었습니다."))
                    _sideEffect.emit(ParentRewardSideEffect.NavigateUp)
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun validateAndFixPrice() {
        val text = priceState.text.toString()
        val currentPrice = text.toIntOrNull()

        if (currentPrice == null || currentPrice < RewardPriceDefaults.MIN_PRICE) {
            priceState.setTextAndPlaceCursorAtEnd(RewardPriceDefaults.MIN_PRICE.toString())
        } else if (currentPrice > RewardPriceDefaults.MAX_PRICE) {
            priceState.setTextAndPlaceCursorAtEnd(RewardPriceDefaults.MAX_PRICE.toString())
        }
    }
}