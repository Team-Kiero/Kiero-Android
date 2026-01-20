package com.kiero.data.mission.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias MissionBulkCreateResponseDto = List<CreatedMissionDto>

@Serializable
data class CreatedMissionDto(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("reward")
    val reward: Int,
    @SerialName("dueAt")
    val dueAt: String,
    @SerialName("isCompleted")
    val isCompleted: Boolean
)