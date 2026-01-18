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
    private val json: Json
) : AlarmDataSource {

    override suspend fun getAlarmFeed(
        childId: Long,
        size: Int?,
        cursor: String?
    ): BaseResponse<AlarmFeedResponseDto> =
        service.getAlarmFeed(childId, size, cursor)

    override fun subscribeAlarmFeed(childId: Long): Flow<FeedItemDto> = flow {
        val response = service.subscribeAlarmFeed(childId = childId)
        val reader = response.byteStream().bufferedReader()

        try {
            while (true) {
                val line = reader.readLine() ?: break

                if (line.startsWith("data:")) {
                    val data = line.substringAfter("data:").trim()

                    if (data.startsWith("{")) {
                        try {
                            val item = json.decodeFromString<FeedItemDto>(data)
                            emit(item)
                        } catch (e: Exception) {
                            Timber.e(e, "SSE JSON 파싱 실패: $data")
                        }
                    } else {
                        Timber.d("SSE 연결 메시지 수신: $data")
                    }
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "SSE 스트림 읽기 실패")
            throw e
        } finally {
            reader.close()
        }
    }.flowOn(Dispatchers.IO)
}