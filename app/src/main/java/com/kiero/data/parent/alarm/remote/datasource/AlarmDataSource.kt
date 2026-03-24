package com.kiero.data.parent.alarm.remote.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.alarm.remote.dto.response.AlarmFeedResponseDto
import com.kiero.data.parent.alarm.remote.dto.response.UnreadAlarmFeedResponseDto

interface AlarmDataSource {
    suspend fun getAlarmFeed(
        childId: Long,
        size: Int?,
        cursor: String?
    ): BaseResponse<AlarmFeedResponseDto>

    suspend fun getUnreadAlarm(): BaseResponse<UnreadAlarmFeedResponseDto>
}