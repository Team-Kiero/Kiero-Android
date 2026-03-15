package com.kiero.data.kid.schedule.model

import com.kiero.data.kid.schedule.remote.dto.response.ScheduleProgressResponseDto

data class ScheduleProgressModel(
    val scheduleCount: Int,
    val schedules: List<ScheduleProgressItemModel>
)

data class ScheduleProgressItemModel(
    val name: String,
    val startTime: String,
    val endTime: String,
    val isOngoing: Boolean,
    val stoneType: String,
    val status: String
)

fun ScheduleProgressResponseDto.toModel() = ScheduleProgressModel(
    scheduleCount = this.scheduleCount,
    schedules = this.schedules.map {
        ScheduleProgressItemModel(
            name = it.name,
            startTime = it.startTime,
            endTime = it.endTime,
            isOngoing = it.isOngoing,
            stoneType = it.stoneType,
            status = it.status
        )
    }
)