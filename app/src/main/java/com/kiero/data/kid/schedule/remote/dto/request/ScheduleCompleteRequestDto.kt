package com.kiero.data.kid.schedule.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleCompleteRequestDto(
    @SerialName("imageUrl")
    val imageUrl: String
)