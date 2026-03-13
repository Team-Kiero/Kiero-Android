package com.kiero.presentation.parent.screen.reward.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.common.extension.toHandleErrorMessage
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.data.parent.reward.repository.RewardRepository
import com.kiero.presentation.parent.screen.reward.model.ParentRewardUiModel
import com.kiero.presentation.parent.screen.reward.state.ParentRewardAddSideEffect
import com.kiero.presentation.parent.screen.reward.state.ParentRewardAddState
import com.kiero.presentation.parent.screen.reward.state.ParentRewardEditSideEffect
import com.kiero.presentation.parent.screen.reward.state.ParentRewardEditState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ParentRewardAddEditViewModel @Inject constructor(
    private val rewardRepository: RewardRepository,
    private val userInfoManager: UserInfoManager,
) : ViewModel() {

    private val _addState = MutableStateFlow(ParentRewardAddState())
    val addState: StateFlow<ParentRewardAddState> = _addState.asStateFlow()

    private val _editState = MutableStateFlow(ParentRewardEditState())
    val editState: StateFlow<ParentRewardEditState> = _editState.asStateFlow()

    private val _addSideEffect = MutableSharedFlow<ParentRewardAddSideEffect>()
    val addSideEffect = _addSideEffect.asSharedFlow()

    private val _editSideEffect = MutableSharedFlow<ParentRewardEditSideEffect>()
    val editSideEffect = _editSideEffect.asSharedFlow()

    fun initReward(reward: ParentRewardUiModel) {
        _editState.update {
            it.copy(
                couponId = reward.couponId,
                name = reward.name,
                price = reward.price,
            )
        }
    }

    fun onAddNameChanged(name: String) {
        _addState.update { it.copy(name = name) }
    }

    fun onAddPriceChanged(price: Int) {
        _addState.update { it.copy(price = price) }
    }

    fun onEditNameChanged(name: String) {
        _editState.update { it.copy(name = name) }
    }

    fun onEditPriceChanged(price: Int) {
        _editState.update { it.copy(price = price) }
    }

    fun createReward(name: String, price: Int) {
        viewModelScope.launch {
            if (name.isBlank()) {
                _addSideEffect.emit(ParentRewardAddSideEffect.ShowSnackBar("보상 이름을 입력해주세요."))
                return@launch
            }
            if (price <= 0) {
                _addSideEffect.emit(ParentRewardAddSideEffect.ShowSnackBar("보상을 입력해주세요."))
                return@launch
            }

            val childId = userInfoManager.getChildIdInfo() ?: return@launch
            _addState.update { it.copy(isLoading = true) }

            rewardRepository.createCoupon(
                childId = childId,
                name = name,
                price = price,
            ).onSuccess {
                _addSideEffect.emit(ParentRewardAddSideEffect.ShowSnackBar("보상이 등록되었습니다."))
                _addSideEffect.emit(ParentRewardAddSideEffect.NavigateUp)
            }.onFailure {
                Timber.e("createReward fail: $it")
                _addSideEffect.emit(ParentRewardAddSideEffect.ShowSnackBar(it.toHandleErrorMessage()))
            }

            _addState.update { it.copy(isLoading = false) }
        }
    }

    fun updateReward(name: String, price: Int) {
        viewModelScope.launch {
            val couponId = _editState.value.couponId

            if (name.isBlank()) {
                _editSideEffect.emit(ParentRewardEditSideEffect.ShowSnackBar("보상 이름을 입력해주세요."))
                return@launch
            }
            if (price <= 0) {
                _editSideEffect.emit(ParentRewardEditSideEffect.ShowSnackBar("보상을 입력해주세요."))
                return@launch
            }

            _editState.update { it.copy(isLoading = true) }

            rewardRepository.updateCoupon(
                couponId = couponId,
                name = name,
                price = price,
            ).onSuccess {
                _editSideEffect.emit(ParentRewardEditSideEffect.ShowSnackBar("보상이 수정되었습니다."))
                _editSideEffect.emit(ParentRewardEditSideEffect.NavigateUp)
            }.onFailure {
                Timber.e("updateReward fail: $it")
                _editSideEffect.emit(ParentRewardEditSideEffect.ShowSnackBar(it.toHandleErrorMessage()))
            }

            _editState.update { it.copy(isLoading = false) }
        }
    }
}