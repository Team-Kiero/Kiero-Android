package com.kiero.data.parent.plan.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlanAddRequestDto(
    @SerialName("name")
    val name: String,
    @SerialName("isRecurring")
    val isRecurring: Boolean,
    @SerialName("firstOrderDate")
    val firstOrderDate: String?,
    @SerialName("startTime")
    val startTime: String,
    @SerialName("endTime")
    val endTime: String,
    @SerialName("scheduleColor")
    val scheduleColor: String,
    @SerialName("dayOfWeek")
    val dayOfWeek: String?,
    @SerialName("dates")
    val dates: String?,
)