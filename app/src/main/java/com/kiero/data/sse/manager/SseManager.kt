package com.kiero.data.sse.manager

import com.kiero.data.sse.model.SseEvent
import com.kiero.data.sse.repository.SseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.currentCoroutineContext
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
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class SseManager @Inject constructor(
    private val sseRepository: SseRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var activeSseJob: Job? = null
    private var tokenRefreshJob: Job? = null
    private var cachedAccessToken: String? = null
    private var lastEventId: String? = null

    private val mutex = Mutex()

    // 부모 이벤트
    private val _parentInviteEvents = MutableSharedFlow<SseEvent.Parent.Invite>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val parentInviteEvents: SharedFlow<SseEvent.Parent.Invite> = _parentInviteEvents.asSharedFlow()

    private val _parentFeedEvents = MutableSharedFlow<SseEvent.Parent.Feed>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val parentFeedEvents: SharedFlow<SseEvent.Parent.Feed> = _parentFeedEvents.asSharedFlow()

    private val _parentScheduleEvents = MutableSharedFlow<SseEvent.Parent.Schedule>(
        replay = 0,
        extraBufferCapacity = 1
    )

    val parentScheduleEvents = _parentScheduleEvents.asSharedFlow()



    // 자녀 이벤트
    private val _childMissionEvents = MutableSharedFlow<SseEvent.Kid.Mission>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val childMissionEvents: SharedFlow<SseEvent.Kid.Mission> = _childMissionEvents.asSharedFlow()

    private val _childScheduleEvents = MutableSharedFlow<SseEvent.Kid.Schedule>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val childScheduleEvents: SharedFlow<SseEvent.Kid.Schedule> = _childScheduleEvents.asSharedFlow()

    private val _childCouponEvents = MutableSharedFlow<SseEvent.Kid.Coupon>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val childCouponEvents: SharedFlow<SseEvent.Kid.Coupon> = _childCouponEvents.asSharedFlow()

    private val _childDateEvents = MutableSharedFlow<SseEvent.Kid.Date>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val dateEvents: SharedFlow<SseEvent.Kid.Date> = _childDateEvents.asSharedFlow()

    // 연결 상태
    private val _connectionState = MutableSharedFlow<Boolean>(replay = 1)
    val connectionState: SharedFlow<Boolean> = _connectionState.asSharedFlow()

    private var isSubscribed = false
    private var isParentMode = true

    fun startParentSubscription() {
        initSubscription(isParent = true)
    }

    fun startChildSubscription() {
        initSubscription(isParent = false)
    }

    private fun initSubscription(isParent: Boolean) {
        scope.launch {
            val jobToCancel = mutex.withLock {
                if (isSubscribed && isParentMode == isParent) return@launch
                stopSubscriptionInternal()
            }
            jobToCancel?.cancelAndJoin()

            mutex.withLock {
                isSubscribed = true
                isParentMode = isParent
                cachedAccessToken = null
                activeSseJob = launch { subscriptionLoop(isParent) }
                startTokenRefreshTimer()
            }
        }
    }
    private suspend fun subscriptionLoop(isParent: Boolean, oldJobToCancel: Job? = null) {
        var retryCount = 0
        val maxRetry = 5

        while (currentCoroutineContext().isActive && isSubscribed) {
            try {
                val token = getValidToken()
                if (token == null) {
                    retryCount++
                    if (retryCount >= maxRetry) {
                        Timber.e("SSE 최대 재시도 초과 → 구독 중지")
                        isSubscribed = false
                        cachedAccessToken = null
                        _connectionState.emit(false)
                        tokenRefreshJob?.cancel()
                        tokenRefreshJob = null
                        return
                    }
                    delay(3000L * (1 shl (retryCount - 1)).coerceAtMost(16))
                    continue
                }
                retryCount = 0
                Timber.d("🔄 SSE 연결 시도 (Last-Event-ID: $lastEventId)")

                sseRepository.subscribeEvents(token, lastEventId)
                    .collect { event ->
                        event.eventId?.let { id ->
                            lastEventId = id
                            Timber.d("Last-Event-ID 갱신: $lastEventId")
                        }
                        // 새 연결이 성공적으로 맺어졌을 때만 기존 연결을 종료
                        if (event is SseEvent.Connected) {
                            if (oldJobToCancel != null) {
                                oldJobToCancel.cancel()
                                Timber.d("무중단 재연결 성공, 기존 연결 종료")
                            } else {
                                Timber.d("SSE 연결 성공")
                            }
                            _connectionState.emit(true)
                        }
                        if (isParent) handleParentEvent(event)
                        else handleChildEvent(event)
                    }

            } catch (e: Exception) {
                if (e is CancellationException) throw e

                retryCount++
                Timber.e(e, "SSE 에러 (시도 $retryCount/$maxRetry)")
                _connectionState.emit(false)

                if (retryCount >= maxRetry) {
                    Timber.e("SSE 최대 재시도 초과 → 구독 중지")
                    isSubscribed = false
                    cachedAccessToken = null
                    _connectionState.emit(false)
                    tokenRefreshJob?.cancel()
                    tokenRefreshJob = null
                    return
                }

                if (isTokenExpiredError(e)) cachedAccessToken = null

                // Exponential Backoff: 3s, 6s, 12s, 24s, 48s
                val backoffDelay = 3000L * (1 shl (retryCount - 1)).coerceAtMost(16)
                Timber.d("⏳ ${backoffDelay}ms 후 재시도")
                delay(backoffDelay)
            }
        }
    }

    private fun startTokenRefreshTimer() {
        tokenRefreshJob?.cancel()
        tokenRefreshJob = scope.launch {
            while (isActive) {
                delay(270_000L)
                Timber.d("⏰ SSE 토큰 갱신 주기 도래 (5분)")

                mutex.withLock {
                    if (isSubscribed) {
                        cachedAccessToken = null
                        val oldJob = activeSseJob
                        activeSseJob = launch {
                            Timber.e("새 SSE 연결 시작, 기존은 connected 수신 후 종료")
                            subscriptionLoop(isParentMode, oldJob)
                        }
                    }
                }
            }
        }
    }

    private suspend fun getValidToken(): String? {
        cachedAccessToken?.let { return it }

        return try {
            val result = sseRepository.issueSubscribeToken()
            result.getOrNull()?.also { newToken ->
                cachedAccessToken = newToken
                Timber.d("새 SSE 토큰 발급 완료")
            }
        } catch (e: Exception) {
            Timber.e(e, "토큰 발급 실패")
            delay(5000L)
            null
        }
    }

    private fun isTokenExpiredError(t: Throwable): Boolean {
        return t.message?.contains("403") == true || t.message?.contains("Unauthorized") == true
    }

    fun stopSubscription() {
        scope.launch {
            val jobToCancel = mutex.withLock {
                stopSubscriptionInternal()
            }
            jobToCancel?.cancelAndJoin()
        }
    }

    private fun stopSubscriptionInternal(): Job? {
        isSubscribed = false
        cachedAccessToken = null
        tokenRefreshJob?.cancel()
        tokenRefreshJob = null
        _connectionState.tryEmit(false)

        val jobToCancel = activeSseJob
        activeSseJob = null
        return jobToCancel
    }
    private suspend fun handleParentEvent(event: SseEvent) {
        when (event) {
            is SseEvent.Connected -> Timber.d("부모 SSE Connected")
            is SseEvent.Parent.Invite -> _parentInviteEvents.emit(event)
            is SseEvent.Parent.Feed -> _parentFeedEvents.emit(event)
            is SseEvent.Parent.Schedule -> _parentScheduleEvents.emit(event)
            else -> Timber.w("부모 모드에서 알 수 없는 이벤트: $event")
        }
    }

    private suspend fun handleChildEvent(event: SseEvent) {
        when (event) {
            is SseEvent.Connected -> Timber.d("아이 SSE Connected")
            is SseEvent.Kid.Mission -> _childMissionEvents.emit(event)
            is SseEvent.Kid.Schedule -> _childScheduleEvents.emit(event)
            is SseEvent.Kid.Coupon -> _childCouponEvents.emit(event)
            is SseEvent.Kid.Date -> _childDateEvents.emit(event)
            else -> Timber.w("자녀 모드에서 알 수 없는 이벤트: $event")
        }
    }
}
