package com.kiero.data.schedule.model

import com.kiero.data.schedule.dto.response.ScheduleSkipResponseDto

data class ScheduleSkipModel (
    val scheduleDetailId: Long?
)

fun ScheduleSkipResponseDto.toModel() = ScheduleSkipModel(
    scheduleDetailId = this.scheduleDetailId
)