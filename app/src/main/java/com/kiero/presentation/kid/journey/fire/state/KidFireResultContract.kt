package com.kiero.presentation.kid.journey.fire.state

import androidx.compose.runtime.Immutable
import com.kiero.presentation.kid.journey.fire.model.KidFireUiModel
import com.kiero.presentation.kid.journey.model.KidJourneyStoneType
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class KidFireResultState(
    val date: String = "",
    val content : KidFireUiModel = KidFireUiModel()
) {
    companion object{
        val FAKE = KidFireResultState(
            content = KidFireUiModel(
                earnedStones = persistentListOf(
                    KidJourneyStoneType.WISDOM,
                    KidJourneyStoneType.COURAGE,
                    KidJourneyStoneType.GRIT
                ),
                earnedCoin = 10
            )
        )
    }
}