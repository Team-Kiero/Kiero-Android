package com.kiero.presentation.parent.schedule.mission.component.model

object MissionLocalData {
    val fakeMissionEvent = listOf(
        MissionsByDate(
            dueAt = "2026-01-15",
            dayOfWeek = "목요일",
            missions = listOf(
                Mission(1, "장보기", 50, false),
                Mission(2, "설거지하기", 50, true)
            )
        )
    )
}
