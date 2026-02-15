package com.kiero.data.kid.schedule.model

import com.kiero.data.kid.schedule.remote.dto.response.ScheduleSkipResponseDto

data class ScheduleSkipModel (
    val scheduleDetailId: Long?
)

fun ScheduleSkipResponseDto.toModel() = ScheduleSkipModel(
    scheduleDetailId = this.scheduleDetailId
)