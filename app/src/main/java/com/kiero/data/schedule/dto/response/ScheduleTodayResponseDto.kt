package com.kiero.data.schedule.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleTodayResponseDto(
    @SerialName("scheduleDetailId")
    val scheduleDetailId: Long?,
    @SerialName("scheduleOrder")
    val scheduleOrder: Int,
    @SerialName("startTime")
    val startTime: String?,
    @SerialName("endTime")
    val endTime: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("stoneType")
    val stoneType: String?,
    @SerialName("totalSchedule")
    val totalSchedule: Int,
    @SerialName("earnedStones")
    val earnedStones: Int?,
    @SerialName("scheduleStatus")
    val scheduleStatus: String,
    @SerialName("isSkippable")
    val isSkippable: Boolean,
    @SerialName("isNowScheduleVerified")
    val isNowScheduleVerified: Boolean
)
