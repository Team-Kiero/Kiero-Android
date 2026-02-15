package com.kiero.presentation.kid.journey.fire.model

import com.kiero.domain.entity.kid.schedule.ScheduleFireModel
import com.kiero.presentation.kid.journey.model.StoneUiType

data class KidFireUiModel(
    val earnedStones: List<StoneUiType> = listOf(),
    val earnedCoin: Int = 10
)

fun ScheduleFireModel.toUiModel(): KidFireUiModel {
    return KidFireUiModel(
        earnedStones = this.gotStones.map { StoneUiType.valueOf(it) },
        earnedCoin = this.earnedCoinAmount
    )
}