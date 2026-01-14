package com.kiero.data.alarm.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.alarm.dto.response.AlarmFeedResponseDto

interface AlarmDataSource {
    suspend fun getAlarmFeed(
        token: String,
        childId: Long,
        size: Int?,
        cursor: String?
    ): BaseResponse<AlarmFeedResponseDto>
}