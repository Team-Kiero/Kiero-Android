package com.kiero.presentation.parent.schedule.mission.component.model

import kotlinx.collections.immutable.persistentListOf


data class MissionData(
    val missionsByDate: List<MissionsByDate>,
)

data class MissionsByDate(
    val dueAt: String,
    val dayOfWeek: String,
    val missions: List<Mission>,

    ) {
    companion object {
        val fakeMissionEvent = persistentListOf(
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
}

data class Mission(
    val id: Long,
    val name: String,
    val reward: Int,
    val isCompleted: Boolean,
)