package com.kiero.data.parent.mission.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MissionBulkCreateRequestDto(
    @SerialName("missions")
    val missions: List<MissionCreateDto>
)

@Serializable
data class MissionCreateDto(
    @SerialName("name")
    val name: String,
    @SerialName("reward")
    val reward: Int,
    @SerialName("dueAt")
    val dueAt: String
)