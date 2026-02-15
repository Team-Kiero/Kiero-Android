package com.kiero.data.parent.mission.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MissionSuggestionResponseDto(
    @SerialName("suggestedMissions")
    val suggestedMissions: List<SuggestedMissionDto>
)

@Serializable
data class SuggestedMissionDto(
    @SerialName("name")
    val name: String,
    @SerialName("dueAt")
    val dueAt: String,
    @SerialName("reward")
    val reward: Int
)