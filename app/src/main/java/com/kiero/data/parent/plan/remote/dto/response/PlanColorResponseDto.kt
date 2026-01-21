package com.kiero.data.parent.plan.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlanColorResponseDto(
    @SerialName("scheduleColor")
    val scheduleColor: String,
    @SerialName("colorCode")
    val colorCode: String,
)