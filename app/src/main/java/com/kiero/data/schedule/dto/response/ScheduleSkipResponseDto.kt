package com.kiero.data.schedule.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleSkipResponseDto (
    @SerialName("scheduleDetailId")
    val scheduleDetailId: Long,
)