package com.kiero.data.parent.mission.model

import com.kiero.data.parent.mission.remote.dto.request.UpdateMissionRequestDto
import com.kiero.data.parent.mission.remote.dto.response.UpdateMissionResponseDto

data class UpdateMissionModel(
    val name: String,
    val reward: Int,
    val dueAt: String,
)

data class UpdateMissionResultModel(
    val id: Long,
    val name: String,
    val reward: Int,
    val dueAt: String,
)

fun UpdateMissionModel.toDto() = UpdateMissionRequestDto(
    name   = name,
    reward = reward,
    dueAt  = dueAt,
)

fun UpdateMissionResponseDto.toModel() = UpdateMissionResultModel(
    id     = id,
    name   = name,
    reward = reward,
    dueAt  = dueAt,
)