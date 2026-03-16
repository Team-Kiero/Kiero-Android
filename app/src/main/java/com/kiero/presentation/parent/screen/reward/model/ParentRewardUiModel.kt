package com.kiero.presentation.parent.screen.reward.model

import androidx.compose.runtime.Immutable
import com.kiero.data.parent.reward.model.RewardModel

@Immutable
data class ParentRewardUiModel(
    val couponId: Long,
    val name: String,
    val price: Int,
)

data class RewardPriceValue(
    val value: Int
) {
    val displayText: String
        get() = if (value > 0) "+$value" else "$value"
}

fun RewardModel.toUiModel() = ParentRewardUiModel(
    couponId = couponId,
    name = name,
    price = price,
)

object RewardPriceDefaults {
    const val MIN_PRICE = 1
    const val MAX_PRICE = 500
    const val DEFAULT_PRICE = 20
    const val MAX_LENGTH = 3

    val PRESET_VALUES = listOf(
        RewardPriceValue(-10),
        RewardPriceValue(-5),
        RewardPriceValue(5),
        RewardPriceValue(10)
    )

    fun applyChange(currentValue: Int, change: Int): Int {
        return (currentValue + change).coerceIn(MIN_PRICE, MAX_PRICE)
    }
}