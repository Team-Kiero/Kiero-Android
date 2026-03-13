package com.kiero.presentation.parent.screen.reward.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.common.extension.toHandleErrorMessage
import com.kiero.core.common.extension.updateSuccess
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.core.model.UiState
import com.kiero.data.parent.reward.repository.RewardRepository
import com.kiero.presentation.parent.screen.reward.model.ParentRewardUiModel
import com.kiero.presentation.parent.screen.reward.model.toUiModel
import com.kiero.presentation.parent.screen.reward.state.ParentRewardSideEffect
import com.kiero.presentation.parent.screen.reward.state.ParentRewardState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParentRewardViewModel @Inject constructor(
    private val rewardRepository: RewardRepository,
    private val userInfoManager: UserInfoManager,
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<ParentRewardState>>(UiState.Loading)
    val state: StateFlow<UiState<ParentRewardState>> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ParentRewardSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        fetchRewards()
    }

    fun fetchRewards() {
        viewModelScope.launch {
            val childId = userInfoManager.getChildIdInfo() ?: return@launch
            rewardRepository.getCoupons(childId)
                .onSuccess { result ->
                    _state.update {
                        UiState.Success(
                            data = ParentRewardState(
                                rewards = result.map { it.toUiModel() }
                                    .sortedWith(compareBy<ParentRewardUiModel> { it.price }
                                        .thenByDescending { it.couponId })
                                    .toImmutableList()
                            )
                        )
                    }
                }
                .onFailure {
                    _sideEffect.emit(ParentRewardSideEffect.ShowSnackBar(it.toHandleErrorMessage()))
                }
        }
    }

    fun selectReward(reward: ParentRewardUiModel?) {
        _state.updateSuccess { it.copy(selectedReward = reward) }
    }

    fun deleteReward() {
        viewModelScope.launch {
            val couponId = (_state.value as? UiState.Success)?.data?.selectedReward?.couponId ?: return@launch
            rewardRepository.deleteCoupon(couponId)
                .onSuccess {
                    _sideEffect.emit(ParentRewardSideEffect.ShowSnackBar("보상이 삭제되었습니다."))
                    fetchRewards()
                }
                .onFailure {
                    _sideEffect.emit(ParentRewardSideEffect.ShowSnackBar(it.toHandleErrorMessage()))
                }
        }
    }
}