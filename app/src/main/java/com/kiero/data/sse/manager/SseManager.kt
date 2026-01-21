package com.kiero.data.sse.manager

import com.kiero.data.sse.model.SseEvent
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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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

    private val mutex = Mutex()

    // 부모 이벤트
    private val _parentInviteEvents = MutableSharedFlow<SseEvent.Invite>(replay = 1)
    val parentInviteEvents: SharedFlow<SseEvent.Invite> = _parentInviteEvents.asSharedFlow()

    private val _parentFeedEvents = MutableSharedFlow<SseEvent.Feed>(replay = 1)
    val parentFeedEvents: SharedFlow<SseEvent.Feed> = _parentFeedEvents.asSharedFlow()

    // 자녀 이벤트
    private val _childMissionEvents = MutableSharedFlow<SseEvent.Mission>(replay = 1)
    val childMissionEvents: SharedFlow<SseEvent.Mission> = _childMissionEvents.asSharedFlow()

    private val _childScheduleEvents = MutableSharedFlow<SseEvent.Schedule>(replay = 1)
    val childScheduleEvents: SharedFlow<SseEvent.Schedule> = _childScheduleEvents.asSharedFlow()

    // 연결 상태
    private val _connectionState = MutableSharedFlow<Boolean>(replay = 1)
    val connectionState: SharedFlow<Boolean> = _connectionState.asSharedFlow()

    private var isSubscribed = false
    private var isParentMode = true

    fun startParentSubscription() {
        scope.launch {
            mutex.withLock {
                if (isSubscribed) {
                    Timber.d("SSE 이미 구독 중 - 중복 시작 방지")
                    return@launch
                }

                isSubscribed = true
                isParentMode = true
                stopSubscription()

                sseJob = launch {
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
        }
    }

    fun startChildSubscription() {
        scope.launch {
            mutex.withLock {
                if (isSubscribed) {
                    Timber.d("SSE 이미 구독 중 - 중복 시작 방지")
                    return@launch
                }

                isSubscribed = true
                isParentMode = false
                stopSubscription()

                sseJob = launch {
                    sseRepository.issueSubscribeToken()
                        .onSuccess { accessToken ->
                            _connectionState.emit(true)
                            startTokenRefreshTimer()

                            sseRepository.subscribeEvents(accessToken)
                                .collect { event ->
                                    handleChildEvent(event)
                                }
                        }
                        .onFailure { e ->
                            Timber.e(e, "SSE 구독 실패")
                            isSubscribed = false
                            _connectionState.emit(false)
                        }
                }
            }
        }
    }
    private fun startTokenRefreshTimer() {
        tokenRefreshJob?.cancel()

        tokenRefreshJob = scope.launch {
            while (isActive) {
                delay(180_000L)
                Timber.d("🔄 SSE 토큰 자동 갱신 (3분 주기)")
                restartSubscription()
            }
        }
    }

    private fun restartSubscription() {
        Timber.d("🔄 SSE 재연결 시작")
        sseJob?.cancel()

        isSubscribed = false

        if (isParentMode) {
            startParentSubscription()
        } else {
            startChildSubscription()
        }
    }

    private suspend fun handleParentEvent(event: SseEvent) {
        when (event) {
            is SseEvent.Connected -> {
                Timber.d("SSE 연결 완료")
            }

            is SseEvent.Invite -> {
                Timber.d("📨 초대 이벤트: childId=${event.data.childId}")
                _parentInviteEvents.emit(event)
            }

            is SseEvent.Feed -> {
                Timber.d("📢 피드 이벤트: ${event.data.eventType}")
                _parentFeedEvents.emit(event)
            }

            is SseEvent.Mission,
            is SseEvent.Schedule -> {
                Timber.w("부모 SSE에서 자녀 이벤트 수신: $event")
            }
        }
    }

    private suspend fun handleChildEvent(event: SseEvent) {
        when (event) {
            is SseEvent.Connected -> {
                Timber.d("SSE 연결 완료")
            }

            is SseEvent.Mission -> {
                Timber.d("📋 미션 이벤트: ${event.data.missionName}, 보상: ${event.data.reward}금화")
                _childMissionEvents.emit(event)
            }

            is SseEvent.Schedule -> {
                Timber.d("📅 일정 이벤트: ${event.data.scheduleName}")
                _childScheduleEvents.emit(event)
            }

            is SseEvent.Invite,
            is SseEvent.Feed -> {
                Timber.w("자녀 SSE에서 부모 이벤트 수신: $event")
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