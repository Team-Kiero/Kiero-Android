package com.kiero.data.parent.alarm.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.alarm.dto.response.AlarmFeedResponseDto

interface AlarmDataSource {
    suspend fun getAlarmFeed(
        childId: Long,
        size: Int?,
        cursor: String?
    ): BaseResponse<AlarmFeedResponseDto>
}