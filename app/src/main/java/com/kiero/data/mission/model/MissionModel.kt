package com.kiero.data.mission.model

import com.kiero.data.mission.remote.dto.response.CreatedMissionDto
import com.kiero.data.mission.remote.dto.response.MissionByDateResponseDto
import com.kiero.data.mission.remote.dto.response.MissionListResponseDto
import com.kiero.data.mission.remote.dto.response.MissionResponseDto

data class MissionByDateModel(
    val missionsByDate: List<MissionListModel>
)

data class MissionListModel(
    val dueAt: String,
    val dayOfWeek: String,
    val missions: List<MissionModel>
)

data class MissionModel(
    val id: Long,
    val isCompleted: Boolean,
    val name: String,
    val reward: Int
)


fun MissionByDateResponseDto.toModel() = MissionByDateModel(
    missionsByDate = this.missionsByDate.map { it.toModel() }
)

fun MissionListResponseDto.toModel() = MissionListModel(
    dueAt = this.dueAt,
    dayOfWeek = this.dayOfWeek,
    missions = this.missions.map { it.toModel() }
)

fun MissionResponseDto.toModel() = MissionModel(
    id = this.id,
    isCompleted = this.isCompleted,
    name = this.name,
    reward = this.reward
)

fun CreatedMissionDto.toModel() = MissionCompleteModel(
    id = this.id,
    name = this.name,
    reward = this.reward,
    dueAt = this.dueAt,
    isCompleted = this.isCompleted
)