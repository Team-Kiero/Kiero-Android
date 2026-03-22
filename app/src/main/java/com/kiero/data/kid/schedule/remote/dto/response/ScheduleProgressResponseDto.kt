package com.kiero.data.kid.schedule.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleProgressResponseDto(
    @SerialName("scheduleCount")
    val scheduleCount: Int,
    @SerialName("isFireLitToday")
    val isFireLitToday: Boolean,
    @SerialName("schedules")
    val schedules: List<ScheduleProgressItemDto>
)

@Serializable
data class ScheduleProgressItemDto(
    @SerialName("name")
    val name: String,
    @SerialName("startTime")
    val startTime: String,
    @SerialName("endTime")
    val endTime: String,
    @SerialName("isOngoing")
    val isOngoing: Boolean,
    @SerialName("stoneType")
    val stoneType: String,
    @SerialName("status")
    val status: String
)