package com.kiero.data.parent.mission.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MissionSuggestionRequestDto(
    @SerialName("noticeText")
    val noticeText: String
)