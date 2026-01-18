package com.kiero.data.schedule.datasourceimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.schedule.api.ScheduleService
import com.kiero.data.schedule.datasource.ScheduleDataSource
import com.kiero.data.schedule.dto.request.ScheduleCompleteRequestDto
import com.kiero.data.schedule.dto.request.ScheduleImageUploadRequestDto
import com.kiero.data.schedule.dto.response.ScheduleImageUploadResponseDto
import com.kiero.data.schedule.dto.response.TodayScheduleResponseDto
import javax.inject.Inject

class ScheduleDataSourceImpl @Inject constructor(
    private val service: ScheduleService
) : ScheduleDataSource {
    override suspend fun getTodaySchedule(): BaseResponse<TodayScheduleResponseDto> =
        service.getTodaySchedule()

    override suspend fun getPresignedUrl(
        fileName: String,
        contentType: String
    ): BaseResponse<ScheduleImageUploadResponseDto> =
        service.getPresignedUrl(
            request = ScheduleImageUploadRequestDto(
                fileName = fileName,
                contentType = contentType
            )
        )

    override suspend fun postScheduleComplete(
        scheduleDetailId: Long,
        imageUrl: String
    ): BaseResponse<Unit> =
        service.postScheduleComplete(
            scheduleDetailId = scheduleDetailId,
            request = ScheduleCompleteRequestDto(
                imageUrl = imageUrl
            )
        )
}