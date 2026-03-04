package com.kiero.presentation.kid.journey.map.model

import com.kiero.presentation.kid.journey.model.KidJourneyStoneType

data class KidMapItemUiModel(
    val name: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val isOngoing: Boolean = false,
    val stoneType: KidJourneyStoneType = KidJourneyStoneType.COURAGE,
    val status: KidMapScheduleStatus = KidMapScheduleStatus.PENDING
)