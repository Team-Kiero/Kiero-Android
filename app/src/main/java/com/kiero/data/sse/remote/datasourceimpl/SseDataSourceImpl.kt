package com.kiero.data.sse.remote.datasourceimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.sse.remote.api.SseService
import com.kiero.data.sse.remote.datasource.SseDataSource
import com.kiero.data.sse.remote.dto.response.FeedDataDto
import com.kiero.data.sse.remote.dto.response.InviteDataDto
import com.kiero.data.sse.remote.dto.response.SseEvent
import com.kiero.data.sse.remote.dto.response.SseTokenResponseDto
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import timber.log.Timber
import javax.inject.Inject

class SseDataSourceImpl @Inject constructor(
    private val sseService: SseService,
    private val okHttpClient: OkHttpClient
) : SseDataSource {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    override suspend fun issueSubscribeToken(): BaseResponse<SseTokenResponseDto> {
        return sseService.issueSubscribeToken()
    }

    override suspend fun subscribeEvents(
        accessToken: String
    ): Flow<SseEvent> = callbackFlow {
        val request = Request.Builder()
            .url("${getBaseUrl()}/api/v1/subscribe")
            .header("Accept", "text/event-stream")
            .header("Authorization", "Bearer $accessToken")
            .build()

        val eventSource = EventSources.createFactory(okHttpClient)
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
                    Timber.d("SSE 이벤트 수신 - type: $type, data: $data")

                    val event = when (type) {
                        "connected" -> {
                            SseEvent.Connected
                        }
                        "invite" -> {
                            try {
                                val inviteData = json.decodeFromString<InviteDataDto>(data)
                                SseEvent.Invite(inviteData)
                            } catch (e: Exception) {
                                Timber.e(e, "Invite 데이터 파싱 실패: $data")
                                null
                            }
                        }
                        "feed" -> {
                            try {
                                val feedData = json.decodeFromString<FeedDataDto>(data)
                                SseEvent.Feed(feedData)
                            } catch (e: Exception) {
                                Timber.e(e, "Feed 데이터 파싱 실패: $data")
                                null
                            }
                        }
                        else -> {
                            Timber.w("알 수 없는 이벤트 타입: $type")
                            null
                        }
                    }

                    event?.let { trySend(it) }
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
                    Timber.e(t, "SSE 연결 실패 - response: ${response?.code}")
                    close(t)
                }
            })

        awaitClose {
            Timber.d("SSE Flow 종료 - EventSource 취소")
            eventSource.cancel()
        }
    }

    private fun getBaseUrl(): String {
        return com.kiero.BuildConfig.BASE_URL.removeSuffix("/")
    }
}