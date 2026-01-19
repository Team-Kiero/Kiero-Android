package com.kiero.data.schedule.dto.response

import kotlinx.serialization.SerialName

data class ScheduleSkipResponseDto (
    @SerialName("scheduleDetailId")
    val scheduleDetailId: Long,
)