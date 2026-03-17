package com.kiero.presentation.parent.screen.reward.state

import androidx.compose.runtime.Immutable
import com.kiero.presentation.parent.screen.reward.model.ParentRewardUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ParentRewardState(
    val rewards: ImmutableList<ParentRewardUiModel> = persistentListOf(),
    val selectedReward: ParentRewardUiModel? = null,
)

@Immutable
data class ParentRewardFormState(
    val couponId: Long? = null,
    val name: String = "",
    val price: Int = 0,
    val isLoading: Boolean = false,
) {
    val isEditMode: Boolean get() = couponId != null
}

sealed interface ParentRewardSideEffect {
    data class ShowSnackBar(val message: String) : ParentRewardSideEffect
    data object NavigateUp : ParentRewardSideEffect
}
