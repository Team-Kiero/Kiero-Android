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
    private val json: Json // SON 파싱을 위해 주입 필요 (NetworkModule 등에 정의됨)
) : AlarmDataSource {

    override suspend fun getAlarmFeed(
        token: String,
        childId: Long,
        size: Int?,
        cursor: String?
    ): BaseResponse<AlarmFeedResponseDto> =
        service.getAlarmFeed(token, childId, size, cursor)

    // ✅ 실시간 구독 로직 추가
    override fun subscribeAlarmFeed(token: String, childId: Long): Flow<FeedItemDto> = flow {
        val response = service.subscribeAlarmFeed(token = token, childId = childId)
        val reader = response.byteStream().bufferedReader() // 스트림을 한 줄씩 읽기 위한 리더

        try {
            while (true) {
                val line = reader.readLine() ?: break // 연결 종료 시 탈출

                // 서버 명세: "data: {"feedItemId": 10, ...}" 형태 대응
                if (line.startsWith("data:")) {
                    val data = line.substringAfter("data:").trim()

                    // "subscribed" 같은 단순 메시지는 넘기고, JSON 형태만 파싱
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
            reader.close() // 스트림 닫기
        }
    }.flowOn(Dispatchers.IO) // 네트워크 작업이므로 IO 스레드에서 실행
}