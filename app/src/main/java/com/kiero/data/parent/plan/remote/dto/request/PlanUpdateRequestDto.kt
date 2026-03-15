package com.kiero.data.parent.plan.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlanUpdateRequestDto(
    @SerialName("name")
    val name: String,
    @SerialName("isRecurring")
    val isRecurring: Boolean,
    @SerialName("startTime")
    val startTime: String,
    @SerialName("endTime")
    val endTime: String,
    @SerialName("scheduleColor")
    val scheduleColor: String,
    @SerialName("dayOfWeek")
    val dayOfWeek: String? = null,
    @SerialName("dates")
    val dates: String? = null,
    @SerialName("isIncludeFollowing")
    val isIncludeFollowing: Boolean? = null,
)