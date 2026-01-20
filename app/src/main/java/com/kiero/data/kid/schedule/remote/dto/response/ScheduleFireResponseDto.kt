package com.kiero.data.kid.schedule.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleFireResponseDto (
    @SerialName("gotStones")
    val gotStones: List<String>,
    @SerialName("earnedCoinAmount")
    val earnedCoinAmount: Int
)