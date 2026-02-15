package com.kiero.data.parent.alarm.datasourceimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.alarm.api.AlarmService
import com.kiero.data.parent.alarm.datasource.AlarmDataSource
import com.kiero.data.parent.alarm.dto.response.AlarmFeedResponseDto

import javax.inject.Inject

class AlarmDataSourceImpl @Inject constructor(
    private val service: AlarmService,
) : AlarmDataSource {

    override suspend fun getAlarmFeed(
        childId: Long,
        size: Int?,
        cursor: String?
    ): BaseResponse<AlarmFeedResponseDto> =
        service.getAlarmFeed(childId, size, cursor)
}