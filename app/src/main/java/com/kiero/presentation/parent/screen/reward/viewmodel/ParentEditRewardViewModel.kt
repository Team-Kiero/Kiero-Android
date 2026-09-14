package com.kiero.presentation.parent.screen.reward.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kiero.core.common.state.ClampedNumberFieldState
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.data.parent.reward.repository.RewardRepository
import com.kiero.presentation.parent.screen.reward.model.RewardPriceDefaults
import com.kiero.presentation.parent.screen.reward.navigation.RewardEdit
import com.kiero.presentation.parent.screen.reward.state.ParentRewardFormState
import com.kiero.presentation.parent.screen.reward.state.ParentRewardSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ParentEditRewardViewModel @Inject constructor(
    private val rewardRepository: RewardRepository,
    private val userInfoManager: UserInfoManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val couponId = savedStateHandle.toRoute<RewardEdit>().couponId

    val nameState = TextFieldState()
    val priceField = ClampedNumberFieldState(
        min = RewardPriceDefaults.MIN_PRICE,
        max = RewardPriceDefaults.MAX_PRICE,
    )

    private val _state = MutableStateFlow(ParentRewardFormState())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ParentRewardSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        fetchDetail()
    }

    private fun fetchDetail() {
        viewModelScope.launch {
            val childId = userInfoManager.getChildIdInfo() ?: return@launch
            _state.update { it.copy(isLoading = true) }
            rewardRepository.getCoupons(childId).onSuccess { list ->
                list.find { it.couponId == couponId }?.let { reward ->
                    nameState.setTextAndPlaceCursorAtEnd(reward.name)
                    priceField.setValue(reward.price)
                }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun updateReward() {
        val name = nameState.text.toString().trim()
        val price = priceField.value ?: 0

        viewModelScope.launch {
            if (name.isEmpty()) {
                _sideEffect.emit(ParentRewardSideEffect.ShowSnackBar("보상 이름을 입력해주세요."))
                return@launch
            }
            if (price <= 0) {
                _sideEffect.emit(ParentRewardSideEffect.ShowSnackBar("보상을 입력해주세요."))
                return@launch
            }

            _state.update { it.copy(isLoading = true) }
            rewardRepository.updateCoupon(couponId, name, price)
                .onSuccess {
                    _sideEffect.emit(ParentRewardSideEffect.ShowSnackBar("보상이 수정되었습니다."))
                    _sideEffect.emit(ParentRewardSideEffect.NavigateUp)
                }
                .onFailure {
                    _sideEffect.emit(ParentRewardSideEffect.ShowSnackBar("수정에 실패했습니다."))
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun onPriceClick(change: Int) {
        priceField.applyChange(change)
    }

    fun validateAndFixPrice() {
        val exceedsMax = (priceField.value ?: 0) > RewardPriceDefaults.MAX_PRICE
        priceField.clampToRange()
        if (exceedsMax) {
            viewModelScope.launch {
                _sideEffect.emit(ParentRewardSideEffect.ShowSnackBar("최대 보상은 ${RewardPriceDefaults.MAX_PRICE}개입니다"))
            }
        }
    }
}