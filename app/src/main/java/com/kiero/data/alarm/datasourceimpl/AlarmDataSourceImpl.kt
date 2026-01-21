package com.kiero.data.alarm.datasourceimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.alarm.api.AlarmService
import com.kiero.data.alarm.datasource.AlarmDataSource
import com.kiero.data.alarm.dto.response.AlarmFeedResponseDto
import com.kiero.data.alarm.dto.response.FeedItemDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import timber.log.Timber
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