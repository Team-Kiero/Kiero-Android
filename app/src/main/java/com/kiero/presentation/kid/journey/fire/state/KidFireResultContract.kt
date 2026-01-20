package com.kiero.presentation.kid.journey.fire.state

import androidx.compose.runtime.Immutable
import com.kiero.presentation.kid.journey.model.StoneUiType

@Immutable
data class KidFireResultState(
    val earnedStones: List<StoneUiType> = listOf(),
    val earnedCoin: Int = 0
) {
    companion object{
        fun fake() = KidFireResultState(
            earnedStones = listOf(
                StoneUiType.WISDOM,
                StoneUiType.COURAGE,
                StoneUiType.GRIT
            ),
            earnedCoin = 10
        )
    }
}