package com.kiero.data.schedule.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.schedule.dto.response.ScheduleFireResponseDto
import com.kiero.data.schedule.dto.response.ScheduleImageUploadResponseDto
import com.kiero.data.schedule.dto.response.ScheduleSkipResponseDto
import com.kiero.data.schedule.dto.response.ScheduleTodayResponseDto

interface ScheduleDataSource {
    suspend fun patchScheduleToday(): BaseResponse<ScheduleTodayResponseDto>

    suspend fun postPresignedUrl(
        fileName: String,
        contentType: String
    ): BaseResponse<ScheduleImageUploadResponseDto>

    suspend fun patchScheduleComplete(
        scheduleDetailId: Long,
        imageUrl: String
    ): BaseResponse<Unit>

    suspend fun patchScheduleSkip(
        scheduleDetailId: Long
    ): BaseResponse<ScheduleSkipResponseDto>

    suspend fun patchScheduleFireLit(): BaseResponse<ScheduleFireResponseDto>
}
