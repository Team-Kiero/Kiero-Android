package com.kiero.data.sse.manager

import com.kiero.data.sse.remote.dto.response.SseEvent
import com.kiero.data.sse.repository.SseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SseManager @Inject constructor(
    private val sseRepository: SseRepository
) {
    private val scope = CoroutineScope(SupervisorJob())

    private var sseJob: Job? = null
    private var tokenRefreshJob: Job? = null

    private val _parentInviteEvents = MutableSharedFlow<SseEvent.Invite>(replay = 0)
    val parentInviteEvents: SharedFlow<SseEvent.Invite> = _parentInviteEvents.asSharedFlow()

    private val _parentFeedEvents = MutableSharedFlow<SseEvent.Feed>(replay = 0)
    val parentFeedEvents: SharedFlow<SseEvent.Feed> = _parentFeedEvents.asSharedFlow()

    private val _connectionState = MutableSharedFlow<Boolean>(replay = 1)
    val connectionState: SharedFlow<Boolean> = _connectionState.asSharedFlow()

    private var isSubscribed = false

    fun startParentSubscription() {
        if (isSubscribed) {
            Timber.d("SSE 이미 구독 중 - 중복 시작 방지")
            return
        }

        isSubscribed = true
        stopSubscription()

        sseJob = scope.launch {
            sseRepository.issueSubscribeToken()
                .onSuccess { accessToken ->
                    _connectionState.emit(true)
                    startTokenRefreshTimer()

                    sseRepository.subscribeEvents(accessToken)
                        .collect { event ->
                            handleParentEvent(event)
                        }
                }
                .onFailure { e ->
                    Timber.e(e, "SSE 구독 실패")
                    isSubscribed = false
                    _connectionState.emit(false)
                }
        }
    }

    private fun startTokenRefreshTimer() {
        tokenRefreshJob?.cancel()

        tokenRefreshJob = scope.launch {
            while (isActive) {
                delay(290_000L) // 4분 50초
                Timber.d("SSE 토큰 자동 갱신")
                restartSubscription()
            }
        }
    }

    private fun restartSubscription() {
        sseJob?.cancel()
        startParentSubscription()
    }

    private suspend fun handleParentEvent(event: SseEvent) {
        when (event) {
            is SseEvent.Connected -> {
                Timber.d("SSE 연결 완료")
            }
            is SseEvent.Invite -> {
                Timber.d("초대 이벤트: ${event.data.childId}")
                _parentInviteEvents.emit(event)
            }
            is SseEvent.Feed -> {
                Timber.d("피드 이벤트: ${event.data.eventType}")
                _parentFeedEvents.emit(event)
            }
        }
    }

    fun stopSubscription() {
        isSubscribed = false
        sseJob?.cancel()
        tokenRefreshJob?.cancel()
        scope.launch {
            _connectionState.emit(false)
        }
        Timber.d("SSE 구독 중지")
    }
}