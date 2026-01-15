package com.kiero.presentation.parent.schedule.mission.component.model

import kotlinx.serialization.Serializable


@Serializable
data class MissionData(
    val missionsByDate: List<MissionsByDate>
)

@Serializable
data class MissionsByDate(
    val dueAt: String,
    val dayOfWeek: String,
    val missions: List<Mission>
)

@Serializable
data class Mission(
    val id: Long,
    val name: String,
    val reward: Int,
    val isCompleted: Boolean
)