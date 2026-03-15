package com.kiero.data.parent.alarm.remote.datasourceimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.alarm.remote.api.AlarmService
import com.kiero.data.parent.alarm.remote.datasource.AlarmDataSource
import com.kiero.data.parent.alarm.remote.dto.response.AlarmFeedResponseDto
import com.kiero.data.parent.alarm.remote.dto.response.UnreadAlarmFeedResponseDto

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

    override suspend fun getUnreadAlarm(): BaseResponse<UnreadAlarmFeedResponseDto> =
        service.getUnreadAlarm()
}