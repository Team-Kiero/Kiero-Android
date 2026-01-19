package com.kiero.data.schedule.model

import com.kiero.data.schedule.dto.response.ScheduleFireResponseDto

data class ScheduleFireModel(
    val gotStones: List<String>,
    val earnedCoinAmount: Int
)

fun ScheduleFireResponseDto.toModel() = ScheduleFireModel(
    gotStones = gotStones,
    earnedCoinAmount = earnedCoinAmount
)