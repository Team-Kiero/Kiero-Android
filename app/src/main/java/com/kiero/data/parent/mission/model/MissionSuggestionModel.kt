package com.kiero.data.parent.mission.model

import com.kiero.data.parent.mission.remote.dto.response.MissionSuggestionResponseDto
import com.kiero.data.parent.mission.remote.dto.response.SuggestedMissionDto


data class MissionSuggestionModel(
    val suggestedMissions: List<SuggestedMissionModel>
)

data class SuggestedMissionModel(
    val name: String,
    val dueAt: String,
    val reward: Int
)

fun MissionSuggestionResponseDto.toModel() = MissionSuggestionModel(
    suggestedMissions = this.suggestedMissions.map { it.toModel() }
)

fun SuggestedMissionDto.toModel() = SuggestedMissionModel(
    name = this.name,
    dueAt = this.dueAt,
    reward = this.reward
)