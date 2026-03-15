package com.kiero.presentation.kid.journey.fire.model

import androidx.compose.runtime.Immutable
import com.kiero.data.kid.schedule.model.ScheduleFireModel
import com.kiero.presentation.kid.journey.model.KidJourneyStoneType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class KidFireUiModel(
    val earnedStones: ImmutableList<KidJourneyStoneType> = persistentListOf(),
    val earnedCoin: Int = 10
)

fun ScheduleFireModel.toUiModel(): KidFireUiModel {
    return KidFireUiModel(
        earnedStones = this.gotStones.mapNotNull { stoneString ->
            runCatching { KidJourneyStoneType.valueOf(stoneString) }.getOrNull()
        }.toImmutableList(),
        earnedCoin = this.earnedCoinAmount
    )
}