package com.kiero.data.mission.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MissionResponseDto(
    @SerialName("id")
    val id : Long,
    @SerialName("name")
    val name: String,
    @SerialName("reward")
    val reward: Int,
    @SerialName("dueAt")
    val dueAt: String,
    @SerialName("isCompleted")
    val isCompleted: Boolean
)