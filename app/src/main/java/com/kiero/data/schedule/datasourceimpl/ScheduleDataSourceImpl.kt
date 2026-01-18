package com.kiero.data.schedule.datasourceimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.schedule.api.ScheduleService
import com.kiero.data.schedule.datasource.ScheduleDataSource
import com.kiero.data.schedule.dto.response.TodayScheduleResponseDto
import javax.inject.Inject

class ScheduleDataSourceImpl @Inject constructor(
    private val service: ScheduleService
) : ScheduleDataSource {
    override suspend fun getTodaySchedule(): BaseResponse<TodayScheduleResponseDto> =
        service.getTodaySchedule()
}