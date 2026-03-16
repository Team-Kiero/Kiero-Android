package com.kiero.data.kid.schedule.remote.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.kid.schedule.remote.dto.response.ScheduleFireResponseDto
import com.kiero.data.kid.schedule.remote.dto.response.ScheduleProgressResponseDto
import com.kiero.data.kid.schedule.remote.dto.response.ScheduleSkipResponseDto
import com.kiero.data.kid.schedule.remote.dto.response.ScheduleTodayResponseDto

interface ScheduleDataSource {
    suspend fun patchScheduleToday(): BaseResponse<ScheduleTodayResponseDto>

    suspend fun patchScheduleComplete(
        scheduleDetailId: Long,
        imageUrl: String
    ): BaseResponse<Unit>

    suspend fun patchScheduleSkip(
        scheduleDetailId: Long
    ): BaseResponse<ScheduleSkipResponseDto>

    suspend fun patchScheduleFireLit(): BaseResponse<ScheduleFireResponseDto>

    suspend fun getScheduleProgress(): BaseResponse<ScheduleProgressResponseDto>
}