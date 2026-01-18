package com.kiero.data.schedule.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.schedule.dto.response.TodayScheduleResponseDto

interface ScheduleDataSource {
    suspend fun getTodaySchedule(): BaseResponse<TodayScheduleResponseDto>
}
