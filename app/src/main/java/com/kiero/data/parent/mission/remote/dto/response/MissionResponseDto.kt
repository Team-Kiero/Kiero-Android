package com.kiero.data.parent.mission.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MissionByDateResponseDto(
    @SerialName("missionsByDate")
    val missionsByDate: List<MissionListResponseDto>,
)

@Serializable
data class MissionListResponseDto(
    @SerialName("dueAt")
    val dueAt: String,
    @SerialName("dayOfWeek")
    val dayOfWeek: String,
    @SerialName("missions")
    val missions: List<MissionResponseDto>
)

@Serializable
data class MissionResponseDto(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("reward")
    val reward: Int,
    @SerialName("isCompleted")
    val isCompleted: Boolean,
)
