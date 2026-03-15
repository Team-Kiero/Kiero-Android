package com.kiero.presentation.parent.screen.journey.model

import com.kiero.data.parent.journey.model.ParentJourneyMissionModel

data class JourneyMissionUiModel(
    val name: String = "",
    val reward: Int = 0
)

fun ParentJourneyMissionModel.toUiModel() = JourneyMissionUiModel(
    name = name,
    reward = reward
)