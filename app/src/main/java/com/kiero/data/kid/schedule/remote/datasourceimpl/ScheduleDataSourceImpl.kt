package com.kiero.data.kid.schedule.remote.datasourceimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.kid.schedule.remote.api.ScheduleService
import com.kiero.data.kid.schedule.remote.datasource.ScheduleDataSource
import com.kiero.data.kid.schedule.remote.dto.request.ScheduleCompleteRequestDto
import com.kiero.data.kid.schedule.remote.dto.response.ScheduleFireResponseDto
import com.kiero.data.kid.schedule.remote.dto.response.ScheduleProgressResponseDto
import com.kiero.data.kid.schedule.remote.dto.response.ScheduleSkipResponseDto
import com.kiero.data.kid.schedule.remote.dto.response.ScheduleTodayResponseDto
import javax.inject.Inject

class ScheduleDataSourceImpl @Inject constructor(
    private val service: ScheduleService
) : ScheduleDataSource {
    override suspend fun patchScheduleToday(): BaseResponse<ScheduleTodayResponseDto> =
        service.patchScheduleToday()

    override suspend fun patchScheduleComplete(
        scheduleDetailId: Long,
        imageUrl: String
    ): BaseResponse<Unit> =
        service.patchScheduleComplete(
            scheduleDetailId = scheduleDetailId,
            request = ScheduleCompleteRequestDto(
                imageUrl = imageUrl
            )
        )

    override suspend fun patchScheduleSkip(
        scheduleDetailId: Long
    ): BaseResponse<ScheduleSkipResponseDto> =
        service.patchScheduleSkip(scheduleDetailId)

    override suspend fun patchScheduleFireLit(): BaseResponse<ScheduleFireResponseDto> =
        service.patchScheduleFireLit()

    override suspend fun getScheduleProgress(): BaseResponse<ScheduleProgressResponseDto> =
        service.getScheduleProgress()
}