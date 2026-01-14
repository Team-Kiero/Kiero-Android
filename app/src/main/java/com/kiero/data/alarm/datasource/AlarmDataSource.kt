package com.kiero.data.alarm.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.alarm.dto.response.AlarmFeedResponseDto
import com.kiero.data.alarm.dto.response.FeedItemDto
import kotlinx.coroutines.flow.Flow

interface AlarmDataSource {
    suspend fun getAlarmFeed(
        token: String,
        childId: Long,
        size: Int?,
        cursor: String?
    ): BaseResponse<AlarmFeedResponseDto>

    // 추가: 실시간 스트림을 Flow 형태로 반환
    fun subscribeAlarmFeed(
        token: String,
        childId: Long
    ): Flow<FeedItemDto>
}