package com.kiero.data.schedule.model

import com.kiero.data.schedule.dto.response.TodayScheduleResponseDto

data class TodayScheduleModel(
    val scheduleDetailId: Long?,
    val scheduleOrder: Int,
    val startTime: String?,
    val endTime: String?,
    val name: String?,
    val stoneType: String?,
    val totalSchedule: Int,
    val earnedStones: Int?,
    val scheduleStatus: ScheduleStatus,
    val isSkippable: Boolean,
    val isNowScheduleVerified: Boolean
)

enum class ScheduleStatus {
    NO_SCHEDULE,
    FIRST_SCHEDULE,
    NEXT_SCHEDULE_EXIST,
    NOW_SCHEDULE_EXIST,
    FIRE_NOT_LIT,
    FIRE_LIT,
    UNKNOWN
}

fun TodayScheduleResponseDto.toModel() = TodayScheduleModel(
    scheduleDetailId = this.scheduleDetailId,
    scheduleOrder = this.scheduleOrder,
    startTime = this.startTime,
    endTime = this.endTime,
    name = this.name,
    stoneType = this.stoneType,
    totalSchedule = this.totalSchedule,
    earnedStones = this.earnedStones,
    scheduleStatus = ScheduleStatus.valueOf(scheduleStatus),
    isSkippable = this.isSkippable,
    isNowScheduleVerified = this.isNowScheduleVerified
)