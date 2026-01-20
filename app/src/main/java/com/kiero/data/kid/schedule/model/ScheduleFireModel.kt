package com.kiero.data.kid.schedule.model

import com.kiero.data.kid.schedule.remote.dto.response.ScheduleFireResponseDto

data class ScheduleFireModel(
    val gotStones: List<String>,
    val earnedCoinAmount: Int
)

fun ScheduleFireResponseDto.toModel() = ScheduleFireModel(
    gotStones = gotStones,
    earnedCoinAmount = earnedCoinAmount
)