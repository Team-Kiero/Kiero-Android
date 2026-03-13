package com.kiero.presentation.parent.screen.reward.state

import androidx.compose.runtime.Immutable
import com.kiero.presentation.parent.screen.reward.model.ParentRewardUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ParentRewardState(
    val rewards: ImmutableList<ParentRewardUiModel> = persistentListOf(),
    val selectedReward: ParentRewardUiModel? = null,
    val isBottomSheetVisible: Boolean = false,
    val isDeleteDialogVisible: Boolean = false,
)

sealed interface ParentRewardSideEffect {
    data class ShowSnackBar(val message: String) : ParentRewardSideEffect
}

@Immutable
data class ParentRewardAddState(
    val name: String = "",
    val price: Int = 0,
    val isLoading: Boolean = false,
)

sealed interface ParentRewardAddSideEffect {
    data class ShowSnackBar(val message: String) : ParentRewardAddSideEffect
    data object NavigateUp : ParentRewardAddSideEffect
}

@Immutable
data class ParentRewardEditState(
    val couponId: Long = 0L,
    val name: String = "",
    val price: Int = 0,
    val isLoading: Boolean = false,
)

sealed interface ParentRewardEditSideEffect {
    data class ShowSnackBar(val message: String) : ParentRewardEditSideEffect
    data object NavigateUp : ParentRewardEditSideEffect
}