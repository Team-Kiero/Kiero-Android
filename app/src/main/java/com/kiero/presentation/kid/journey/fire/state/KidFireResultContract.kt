package com.kiero.presentation.kid.journey.fire.state

import androidx.compose.runtime.Immutable
import com.kiero.presentation.kid.journey.fire.model.KidFireUiModel
import com.kiero.presentation.kid.journey.model.StoneUiType

@Immutable
data class KidFireResultState(
    val date: String = "",
    val content : KidFireUiModel = KidFireUiModel()
) {
    companion object{
        fun fake() = KidFireResultState(
            content = KidFireUiModel(
                earnedStones = listOf(
                    StoneUiType.WISDOM,
                    StoneUiType.COURAGE,
                    StoneUiType.GRIT
                ),
                earnedCoin = 10
            )
        )
    }
}