package com.kiero.data.schedule.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.schedule.dto.response.TodayScheduleResponseDto
import retrofit2.http.PATCH

interface ScheduleService {
    @PATCH("/api/v1/schedules/today")
    suspend fun getTodaySchedule(): BaseResponse<TodayScheduleResponseDto>
}