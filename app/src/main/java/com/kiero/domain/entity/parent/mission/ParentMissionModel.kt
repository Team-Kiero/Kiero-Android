package com.kiero.domain.entity.parent.mission

import com.kiero.data.parent.mission.remote.dto.response.ParentMissionAddResponseDto

data class ParentMissionAddModel(
    val id: Long,
    val name: String,
    val reward: Int,
    val dueAt: String,
    val isCompleted: Boolean
)

fun ParentMissionAddResponseDto.toModel() = ParentMissionAddModel(
    id = this.id,
    name = this.name,
    reward = this.reward,
    dueAt = this.dueAt,
    isCompleted = this.isCompleted
)