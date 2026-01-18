package com.kiero.data.schedule.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.schedule.dto.response.ScheduleImageUploadResponseDto
import com.kiero.data.schedule.dto.response.TodayScheduleResponseDto

interface ScheduleDataSource {
    suspend fun getTodaySchedule(): BaseResponse<TodayScheduleResponseDto>

    suspend fun getPresignedUrl(
        fileName: String,
        contentType: String
    ): BaseResponse<ScheduleImageUploadResponseDto>

    suspend fun postScheduleComplete(
        scheduleDetailId: Long,
        imageUrl: String
    ): BaseResponse<Unit>
}
