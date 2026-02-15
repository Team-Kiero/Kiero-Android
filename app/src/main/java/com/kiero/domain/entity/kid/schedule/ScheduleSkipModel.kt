package com.kiero.domain.entity.kid.schedule

import com.kiero.data.kid.schedule.remote.dto.response.ScheduleSkipResponseDto

data class ScheduleSkipModel (
    val scheduleDetailId: Long?
)

fun ScheduleSkipResponseDto.toModel() = ScheduleSkipModel(
    scheduleDetailId = this.scheduleDetailId
)