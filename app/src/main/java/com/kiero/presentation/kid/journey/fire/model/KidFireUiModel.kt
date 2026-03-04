package com.kiero.presentation.kid.journey.fire.model

import com.kiero.data.kid.schedule.model.ScheduleFireModel
import com.kiero.presentation.kid.journey.model.KidJourneyStoneType

data class KidFireUiModel(
    val earnedStones: List<KidJourneyStoneType> = listOf(),
    val earnedCoin: Int = 10
)

fun ScheduleFireModel.toUiModel(): KidFireUiModel {
    return KidFireUiModel(
        earnedStones = this.gotStones.map { KidJourneyStoneType.valueOf(it) },
        earnedCoin = this.earnedCoinAmount
    )
}