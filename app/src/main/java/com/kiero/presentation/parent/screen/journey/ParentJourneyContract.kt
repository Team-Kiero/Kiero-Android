package com.kiero.presentation.parent.screen.journey

import com.kiero.presentation.parent.screen.journey.model.KidInfo

data class ParentJourneyState (
    val kidInfo: KidInfo = KidInfo(),

    val completedMission: Int = 0,
    val unCompletedMission: Int = 0
)

sealed interface ParentJourneySideEffect {
    object NavigateUp : ParentJourneySideEffect
}
