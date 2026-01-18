package com.kiero.data.mission.model

import com.kiero.data.mission.remote.dto.response.MissionCompleteResponseDto

data class MissionCompleteModel(
    val id : Long,
    val name: String,
    val reward: Int,
    val dueAt: String,
    val isCompleted: Boolean
)

fun MissionCompleteResponseDto.toModel() = MissionCompleteModel(
    id = this.id,
    name = this.name,
    reward = this.reward,
    dueAt = this.dueAt,
    isCompleted = this.isCompleted
)