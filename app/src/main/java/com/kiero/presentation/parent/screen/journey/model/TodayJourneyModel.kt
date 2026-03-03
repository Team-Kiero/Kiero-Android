package com.kiero.presentation.parent.screen.journey.model

data class TodayJourneyModel(
    val date: String = "",
    val todayMission: String = "",
    val isAuthenticated: Boolean = false,
    val authImageUrl: String? = null,
    val todayStatus: TodayStatus = TodayStatus.UPCOMING
)
