package com.kiero.data.sse.remote.datasourceimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.sse.remote.api.SseService
import com.kiero.data.sse.remote.datasource.SseDataSource
import com.kiero.data.sse.model.RawSseEvent
import com.kiero.data.sse.remote.dto.response.SseTokenResponseDto
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import timber.log.Timber
import javax.inject.Inject
import com.kiero.BuildConfig

class SseDataSourceImpl @Inject constructor(
    private val sseService: SseService,
    private val okHttpClient: OkHttpClient,
) : SseDataSource {

    override suspend fun issueSubscribeToken(): BaseResponse<SseTokenResponseDto> {
        return sseService.issueSubscribeToken()
    }

    override suspend fun subscribeEvents(
        accessToken: String,
        lastEventId: String?
    ): Flow<RawSseEvent> = callbackFlow {
        val requestBuilder = Request.Builder()
            .url("${BuildConfig.BASE_URL}api/v1/subscribe")
            .header("Accept", "text/event-stream")
            .header("Authorization", "Bearer $accessToken")

        if (!lastEventId.isNullOrEmpty()) {
            requestBuilder.header("Last-Event-ID", lastEventId)
            Timber.d("헤더에 Last-Event-ID 포함하여 요청: $lastEventId")
        }

        val request = requestBuilder.build()

        val sseClient = okHttpClient.newBuilder()
            .readTimeout(0, java.util.concurrent.TimeUnit.MILLISECONDS)
            .build()

        val eventSource = EventSources.createFactory(sseClient)
            .newEventSource(request, object : EventSourceListener() {

                override fun onOpen(eventSource: EventSource, response: Response) {
                    Timber.d("SSE 연결 성공")
                }

                override fun onEvent(
                    eventSource: EventSource,
                    id: String?,
                    type: String?,
                    data: String
                ) {
                    Timber.d("SSE 원시 데이터 수신 - type: $type, id: $id")

                    trySend(RawSseEvent(type, data, id = id))
                }

                override fun onClosed(eventSource: EventSource) {
                    Timber.d("SSE 연결 종료")
                    close()
                }

                override fun onFailure(
                    eventSource: EventSource,
                    t: Throwable?,
                    response: Response?
                ) {
                    if (t is java.io.IOException && (t.message == "canceled" || t.message == "Canceled")) {
                        Timber.d("SSE 연결이 취소되었습니다 (정상적인 재연결 교체 또는 코루틴 종료)")
                    } else {
                        Timber.e(t, "SSE 연결 실패 - response: ${response?.code}")
                    }

                    close(t)
                }
            })

        awaitClose {
            Timber.d("SSE Flow 종료 - EventSource 취소")
            eventSource.cancel()
        }
    }
}
