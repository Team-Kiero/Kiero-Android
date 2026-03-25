package com.kiero.data.sse.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Mission 이벤트 (MISSION_CREATED)
@Serializable
data class MissionDataDto(
    @SerialName("eventType")
    val eventType: String,

    @SerialName("childId")
    val childId: Long? = null,

    @SerialName("missionName")
    val missionName: String? = null,

    @SerialName("reward")
    val reward: Int? = null
)

// Schedule 이벤트 (SCHEDULE_CREATED)
@Serializable
data class ScheduleDataDto(
    @SerialName("eventType")
    val eventType: String,  // "SCHEDULE_CREATED"

    @SerialName("scheduleName")
    val scheduleName: String? = null,

    @SerialName("childId")
    val childId: Long? = null
)

// Coupon 이벤트 (COUPON_CREATED)
@Serializable
data class CouponDataDto(
    @SerialName("eventType")
    val eventType: String,  // "COUPON_CREATED"

    @SerialName("couponName")
    val couponName: String,

    @SerialName("price")
    val price: Int
)

// Date 이벤트 (DATE_CHANGED)
@Serializable
data class DateDataDto(
    @SerialName("eventType")
    val eventType: String,  // "DATE_CHANGED"
    @SerialName("date")
    val date: String        // "2026-03-14"
)