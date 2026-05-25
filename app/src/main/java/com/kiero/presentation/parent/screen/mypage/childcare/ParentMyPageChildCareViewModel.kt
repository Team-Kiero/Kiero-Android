package com.kiero.presentation.parent.screen.mypage.childcare

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.common.extension.toHandleErrorMessage
import com.kiero.core.common.util.formatTime
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.domain.parent.invite.usecase.GetInviteCode
import com.kiero.presentation.parent.screen.mypage.childcare.model.ParentChildCareStep
import com.kiero.presentation.parent.screen.mypage.childcare.model.ParentMyPageChildInfoModel
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
import javax.inject.Inject

@HiltViewModel
class ParentMyPageChildCareViewModel @Inject constructor(
    private val getInviteCode: GetInviteCode,
    private val userInfoManager: UserInfoManager,
) : ViewModel() {
    private val _state = MutableStateFlow(ParentMyPageChildCareState())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ParentMyPageChildCareSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private var timerJob: Job? = null

    private var lastCopyTime = 0L

    fun fetchChildInfo() {
        viewModelScope.launch {
            val childInfo = userInfoManager.getChildIdInfo()

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

    private fun startTimer() {
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            val targetEndTime = SystemClock.elapsedRealtime() + TIMER_DURATION_MILLIS

            while (isActive) {
                val currentTime = SystemClock.elapsedRealtime()
                val remainingMillis = targetEndTime - currentTime

                if (remainingMillis <= 0) {
                    // 타이머 종료 처리
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
                    _sideEffect.emit(ParentMyPageChildCareSideEffect.NavigateToMyPage)
                }
            }

            ParentChildCareStep.INVITE -> {
                _state.update {
                    it.copy(
                        currentStep = ParentChildCareStep.MANAGEMENT
                    )
                }
            }
        }
    }

    fun onReIssueClick() {
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
                _state.update {
                    it.copy(
                        childInfo = ParentMyPageChildInfoModel(
                            code = result.code,
                            childLastName = result.childLastName,
                            childFirstName = result.childFirstName
                        ),

                        isLoading = false
                    )
                }

                if (_state.value.currentStep == ParentChildCareStep.MANAGEMENT) {
                    _state.update {
                        it.copy(
                            currentStep = ParentChildCareStep.INVITE
                        )
                    }
                }

                startTimer()
            }.onFailure {
                _sideEffect.emit(ParentMyPageChildCareSideEffect.ShowSnackbar(it.toHandleErrorMessage()))
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false
                    )
                }
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
