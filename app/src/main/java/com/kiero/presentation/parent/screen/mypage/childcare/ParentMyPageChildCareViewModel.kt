package com.kiero.presentation.parent.screen.mypage.childcare

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.common.extension.toHandleErrorMessage
import com.kiero.core.common.util.formatTime
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.data.sse.manager.SseManager
import com.kiero.domain.parent.invite.usecase.GetInviteCode
import com.kiero.presentation.parent.screen.mypage.childcare.model.ParentChildCareStep
import com.kiero.presentation.parent.screen.mypage.childcare.model.ParentMyPageChildInfoModel
import com.kiero.presentation.parent.screen.mypage.model.ChildConnectionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ParentMyPageChildCareViewModel @Inject constructor(
    private val getInviteCode: GetInviteCode,
    private val userInfoManager: UserInfoManager,
    private val sseManager: SseManager,
) : ViewModel() {
    private val _state = MutableStateFlow(ParentMyPageChildCareState())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ParentMyPageChildCareSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private var timerJob: Job? = null

    private var lastCopyTime = 0L
    private var shouldClearPendingInviteOnExit = false

    init {
        collectInviteEvents()
        fetchChildInfo()
    }

    private fun collectInviteEvents() {
        viewModelScope.launch {
            sseManager.parentInviteEvents.collect { event ->
                when (event.data.eventType) {
                    "CHILD_JOINED" -> {
                        Timber.d("자녀 가입 완료: ${event.data.childId}")
                        handleChildJoined(event.data.childId)
                    }
                }
            }
        }
    }

    private suspend fun handleChildJoined(childId: Long) {
        userInfoManager.saveChildIdInfo(childId)
        shouldClearPendingInviteOnExit = true
        timerJob?.cancel()
        _state.update {
            it.copy(
                connectionStatus = ChildConnectionStatus.CONNECTED,
                isExpired = false,
                childInfo = it.childInfo.copy(
                    isChildJoined = true
                ),
                currentStep = ParentChildCareStep.MANAGEMENT
            )
        }
        _sideEffect.emit(ParentMyPageChildCareSideEffect.ShowSnackbar("자녀 연동이 완료되었습니다!"))
    }

    fun fetchChildInfo() {
        viewModelScope.launch {
            val childLastName = userInfoManager.getChildLastName() ?: ""
            val childFirstName = userInfoManager.getChildFirstName() ?: ""
            val childId = userInfoManager.getChildIdInfo()

            val pendingCode = userInfoManager.getPendingInviteCode()
            val expireTime = userInfoManager.getPendingInviteExpireTime()
            val currentTime = System.currentTimeMillis()

            Timber.e("childId: $childId, pendingCode: $pendingCode, expireTime: $expireTime, currentTime: $currentTime")

            // 현재 발급받은 유효한 코드가 남아있는가
            if (!pendingCode.isNullOrEmpty() && expireTime > currentTime) {
                _state.update {
                    it.copy(
                        childInfo = ParentMyPageChildInfoModel(
                            code = pendingCode,
                            childLastName = childLastName,
                            childFirstName = childFirstName,
                            isChildJoined = (childId != null)
                        ),
                        connectionStatus = ChildConnectionStatus.PENDING,
                        currentStep = ParentChildCareStep.INVITE,
                        isInitialized = true
                    )
                }
                startTimer(expireTime)
            }
            // 진행 중인 코드는 없지만, 이미 연결된 자녀(childId)가 있는가?
            else if (childId != null) {
                if (expireTime <= currentTime) userInfoManager.clearPendingInviteCode()

                _state.update {
                    it.copy(
                        childInfo = ParentMyPageChildInfoModel(
                            childLastName = childLastName,
                            childFirstName = childFirstName,
                            isChildJoined = true
                        ),
                        connectionStatus = ChildConnectionStatus.CONNECTED,
                        currentStep = ParentChildCareStep.MANAGEMENT,
                        isInitialized = true
                    )
                }
            }
            // 연결된 자녀도 없고, 발급받은 코드도 없는 최초 상태
            else {
                if (expireTime <= currentTime) userInfoManager.clearPendingInviteCode()

                _state.update {
                    it.copy(
                        childInfo = ParentMyPageChildInfoModel(
                            childLastName = childLastName,
                            childFirstName = childFirstName,
                            isChildJoined = false
                        ),
                        connectionStatus = ChildConnectionStatus.CONNECTED,
                        currentStep = ParentChildCareStep.MANAGEMENT,
                        isInitialized = true
                    )
                }
            }
        }
    }

    fun onCopyClick() {
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - lastCopyTime < 500L) return
        lastCopyTime = currentTime

        viewModelScope.launch {
            val copyText = _state.value.childInfo.code
            _sideEffect.emit(
                ParentMyPageChildCareSideEffect.CopyText(
                    message = "코드가 복사되었습니다.",
                    targetText = copyText
                )
            )
        }
    }

    private fun startTimer(targetEndTimeMillis: Long) {
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            while (isActive) {
                val currentTime = System.currentTimeMillis()
                val remainingMillis = targetEndTimeMillis - currentTime

                if (remainingMillis <= 0) {
                    // 타이머 종료 처리
                    userInfoManager.clearPendingInviteCode()
                    _state.update {
                        it.copy(
                            expiredTime = formatTime(0),
                            isExpired = true
                        )
                    }
                    break
                }

                val remainingSeconds = (remainingMillis / 1000).toInt()

                _state.update {
                    it.copy(
                        expiredTime = formatTime(remainingSeconds),
                        isExpired = false
                    )
                }

                delay(200L)
            }
        }
    }

    fun onBackClick() {
        val currentStep = _state.value.currentStep

        when (currentStep) {
            ParentChildCareStep.MANAGEMENT -> {
                viewModelScope.launch {
                    clearPendingInviteCodeIfChildJoined()
                    _sideEffect.emit(ParentMyPageChildCareSideEffect.NavigateToMyPage)
                }
            }

            ParentChildCareStep.INVITE -> {
                viewModelScope.launch {
                    clearPendingInviteCodeIfChildJoined()
                    _sideEffect.emit(ParentMyPageChildCareSideEffect.NavigateToMyPage)
                }
            }
        }
    }

    private suspend fun clearPendingInviteCodeIfChildJoined() {
        if (!shouldClearPendingInviteOnExit) return

        userInfoManager.clearPendingInviteCode()
        shouldClearPendingInviteOnExit = false
        timerJob?.cancel()
    }

    fun onReIssueClick() {
        val currentState = _state.value

        // 기존 code 유효 시 호출 스킵
        if (currentState.currentStep == ParentChildCareStep.MANAGEMENT &&
            currentState.connectionStatus == ChildConnectionStatus.PENDING &&
            !currentState.isExpired &&
            currentState.childInfo.code.isNotEmpty()
        ) {
            _state.update { it.copy(currentStep = ParentChildCareStep.INVITE) }
            return
        }

        // 최초 발급 또는 유효시간이 끝난 재발급 시
        _state.update {
            it.copy(
                isLoading = true,
                isExpired = false
            )
        }

        viewModelScope.launch {
            getInviteCode(
                childLastName = _state.value.childInfo.childLastName,
                childFirstName = _state.value.childInfo.childFirstName
            ).onSuccess { result ->
                val targetEndTime = System.currentTimeMillis() + TIMER_DURATION_MILLIS
                userInfoManager.savePendingInviteCode(result.code, targetEndTime)

                _state.update {
                    it.copy(
                        childInfo = it.childInfo.copy(
                            code = result.code,
                            childLastName = result.childLastName,
                            childFirstName = result.childFirstName
                        ),
                        connectionStatus = ChildConnectionStatus.PENDING,
                        currentStep = ParentChildCareStep.INVITE,
                        isLoading = false
                    )
                }

                // 계산해둔 절대 시간을 넘겨서 타이머 시작
                startTimer(targetEndTime)

            }.onFailure { error ->
                _sideEffect.emit(ParentMyPageChildCareSideEffect.ShowSnackbar(error.toHandleErrorMessage()))
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    companion object {
        private const val TIMER_DURATION_MILLIS = 10 * 60 * 1000L
    }
}
