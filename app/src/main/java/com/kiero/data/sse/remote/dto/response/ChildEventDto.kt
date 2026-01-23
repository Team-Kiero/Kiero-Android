package com.kiero.data.sse.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Mission 이벤트 (MISSION_CREATED)
@Serializable
data class MissionDataDto(
    @SerialName("eventType")
    val eventType: String,  // "MISSION_CREATED"

    @SerialName("missionName")
    val missionName: String,

    @SerialName("reward")
    val reward: Int
)

// Schedule 이벤트 (SCHEDULE_CREATED)
@Serializable
data class ScheduleDataDto(
    @SerialName("eventType")
    val eventType: String,  // "SCHEDULE_CREATED"

    @SerialName("scheduleName")
    val scheduleName: String
)