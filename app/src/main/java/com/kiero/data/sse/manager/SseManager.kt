package com.kiero.data.sse.manager

import com.kiero.data.sse.model.SseEvent
import com.kiero.data.sse.repository.SseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
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
    private val scope = CoroutineScope(SupervisorJob())

    private var sseJob: Job? = null
    private var tokenRefreshJob: Job? = null
    private var cachedAccessToken: String? = null
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
        initSubscription(isParent = true)
    }

    fun startChildSubscription() {
        initSubscription(isParent = false)
    }

    private fun initSubscription(isParent: Boolean) {
        scope.launch {
            mutex.withLock {
                if (isSubscribed && isParentMode == isParent) return@launch

                stopSubscriptionInternal()

                isSubscribed = true
                isParentMode = isParent
                cachedAccessToken = null

                sseJob = launch {
                    subscriptionLoop(isParent)
                }

                startTokenRefreshTimer()
            }
        }
    }
    private suspend fun subscriptionLoop(isParent: Boolean) {
        while (currentCoroutineContext().isActive && isSubscribed) {
            try {
                // 실패 시 재발급으로 continue
                val token = getValidToken() ?: continue

                Timber.d("🔄 SSE 연결 시도 (Token: ${token.take(10)}...)")

                sseRepository.subscribeEvents(token)
                    .collect { event ->
                        _connectionState.emit(true)
                        if (isParent) handleParentEvent(event)
                        else handleChildEvent(event)
                    }

                Timber.w("SSE 스트림 종료됨. 즉시 재연결 시도.")

            } catch (e: Exception) {
                // 상위에 에러 던지기
                if (e is CancellationException) throw e

                Timber.e(e, "SSE 연결 중 에러 발생")
                _connectionState.emit(false)

                if (isTokenExpiredError(e)) {
                    Timber.w("토큰 만료 감지 -> 캐시 삭제 후 재발급 예정")
                    cachedAccessToken = null
                }

                delay(3000L)
            }
        }
    }

    private fun startTokenRefreshTimer() {
        tokenRefreshJob?.cancel()
        tokenRefreshJob = scope.launch {
            while (isActive) {
                delay(180_000L)
                Timber.d("⏰ SSE 토큰 갱신 주기 도래 (3분)")

                mutex.withLock {
                    if (isSubscribed) {
                        cachedAccessToken = null

                        // 현재 job 취소
                        sseJob?.cancel()

                        sseJob = launch {
                            subscriptionLoop(isParentMode)
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
            mutex.withLock {
                stopSubscriptionInternal()
            }
        }
    }

    private suspend fun stopSubscriptionInternal() {
        isSubscribed = false
        cachedAccessToken = null
        sseJob?.cancelAndJoin()
        tokenRefreshJob?.cancel()
        _connectionState.emit(false)
        Timber.d("⛔ SSE 구독 완전 중지")
    }

    private suspend fun handleParentEvent(event: SseEvent) {
        when (event) {
            is SseEvent.Connected -> Timber.d("부모 SSE Connected")
            is SseEvent.Invite -> _parentInviteEvents.emit(event)
            is SseEvent.Feed -> _parentFeedEvents.emit(event)
            else -> Timber.w("부모 모드에서 알 수 없는 이벤트: $event")
        }
    }

    private suspend fun handleChildEvent(event: SseEvent) {
        when (event) {
            is SseEvent.Connected -> Timber.d("아이 SSE Connected")
            is SseEvent.Mission -> _childMissionEvents.emit(event)
            is SseEvent.Schedule -> _childScheduleEvents.emit(event)
            else -> Timber.w("자녀 모드에서 알 수 없는 이벤트: $event")
        }
    }
}
