package com.kiero.data.mission.model

import com.kiero.data.mission.dto.response.MissionResponseDto

data class MissionModel(
    val id : Long,
    val name: String,
    val reward: Int,
    val dueAt: String,
    val isCompleted: Boolean
)

fun MissionResponseDto.toModel() = MissionModel(
    id = this.id,
    name = this.name,
    reward = this.reward,
    dueAt = this.dueAt,
    isCompleted = this.isCompleted
)