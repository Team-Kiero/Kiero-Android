package com.kiero.presentation.signup.parent.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kiero.core.common.extension.toHandleErrorMessage
import com.kiero.core.common.util.formatTime
import com.kiero.core.common.util.suspendRunCatching
import com.kiero.core.common.viewmodel.throttleFirst
import com.kiero.core.localstorage.TokenManager
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.data.sse.manager.SseManager
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.data.parent.signup.repository.ParentSignUpRepository
import com.kiero.presentation.signup.parent.model.ParentSignUpStep
import com.kiero.presentation.signup.parent.model.toUiModel
import com.kiero.presentation.signup.parent.navigation.ParentSignUp
import com.kiero.presentation.signup.parent.state.ParentSignUpSideEffect
import com.kiero.presentation.signup.parent.state.ParentSignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ParentSignUpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ParentSignUpRepository,
    private val authRepository: AuthRepository,
    private val userInfoManager: UserInfoManager,
    private val tokenManager: TokenManager,
    private val sseManager: SseManager,
) : ViewModel() {
    private val _state = MutableStateFlow(ParentSignUpState())
    val state: StateFlow<ParentSignUpState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ParentSignUpSideEffect>()
    val sideEffect: SharedFlow<ParentSignUpSideEffect> = _sideEffect.asSharedFlow()

    private val parentInfo = savedStateHandle.toRoute<ParentSignUp>()

    private var timerJob: Job? = null
    private var copyJob: Job? = null
    private val TIMER_DURATION_SECONDS = 10 * 60

    init {
        initFetchParentInfo(
            parentName = parentInfo.parentName,
            parentProfileImage = parentInfo.parentProfileImage
        )
        sseManager.startParentSubscription()
        collectInviteEvents()
    }

    fun onNextClick() {
        val currentState = _state.value.currentStep

        when (currentState) {
            ParentSignUpStep.ADDCHILD -> {
                postChild()
            }

            ParentSignUpStep.INVITE -> {
                viewModelScope.launch {
                    _sideEffect.emit(ParentSignUpSideEffect.NavigateToParent)
                }
            }
        }
    }

    fun onCopyClick() {
        if (copyJob?.isActive == true) return

        copyJob = throttleFirst {
            val copyText = _state.value.childInfo.code
            _sideEffect.emit(
                ParentSignUpSideEffect.CopyText(
                    message = "코드가 복사되었습니다.",
                    targetText = copyText
                )
            )
        }
    }

    fun initFetchParentInfo(
        parentName: String,
        parentProfileImage: String,
    ) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    parentInfo = it.parentInfo.copy(
                        parentName = parentName,
                        parentProfileImage = parentProfileImage
                    )
                )
            }
        }
    }

    fun postChild() {
        _state.update {
            it.copy(
                isLoading = true,
                isExpired = false
            )
        }

        viewModelScope.launch {
            repository.postSignUp(
                childLastName = _state.value.childInfo.childLastName.text.toString(),
                childFirstName = _state.value.childInfo.childFirstName.text.toString()
            ).onSuccess { result ->
                Timber.d("postChild $result")
                _state.update {
                    it.copy(
                        childInfo = result.toUiModel(),
                        isLoading = false
                    )
                }

                if (_state.value.currentStep == ParentSignUpStep.ADDCHILD) {
                    _state.update {
                        it.copy(
                            currentStep = ParentSignUpStep.INVITE
                        )
                    }
                }

                startTimer()
            }.onFailure {
                _sideEffect.emit(ParentSignUpSideEffect.ShowSnackbar(it.toHandleErrorMessage()))
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false
                    )
                }
            }
        }
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

    fun logOut() {
        sseManager.stopSubscription()

        Timber.e("로그아웃 되었습니다")
        viewModelScope.launch {
            val networkJobs = listOf(
                async { suspendRunCatching { authRepository.postLogout() } },
            )
            networkJobs.awaitAll()
            sseManager.stopSubscription()

            suspendRunCatching { tokenManager.clearTokens() }

            _state.update {
                it.copy(isLoading = false)
            }

            _sideEffect.emit(ParentSignUpSideEffect.NavigateToSelection)
        }
    }

    private fun startTimer() {
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            var remainingTime = TIMER_DURATION_SECONDS

            while (remainingTime >= 0) {
                val formattedTime = formatTime(remainingTime)

                _state.update {
                    it.copy(
                        expiredTime = formattedTime
                    )
                }

                if (remainingTime == 0) {
                    _state.update {
                        it.copy(
                            isExpired = true
                        )
                    }
                    break
                }

                delay(1000L)
                remainingTime--
            }
        }
    }

    fun onProfileClick() {
        _state.update {
            it.copy(isLogoutDialogVisible = true)
        }
    }

    fun onLogoutCancel() {
        _state.update {
            it.copy(isLogoutDialogVisible = false)
        }
    }

    fun onLogoutConfirm() {
        _state.update {
            it.copy(
                isLogoutDialogVisible = false,
                isLoading = true
            )
        }

        logOut()
    }

    fun onBackClick() {
        Timber.e("onBackClick")
        if (_state.value.currentStep == ParentSignUpStep.INVITE) {
            _state.update {
                it.copy(
                    currentStep = ParentSignUpStep.ADDCHILD
                )
            }
        } else {
            viewModelScope.launch {
                _sideEffect.emit(ParentSignUpSideEffect.NavigateToSelection)
            }
        }
    }

    private suspend fun handleChildJoined(childId: Long) {
        userInfoManager.saveChildIdInfo(childId)
        _state.update {
            it.copy(
                isChildJoined = true
            )
        }
        _sideEffect.emit(ParentSignUpSideEffect.OnChildJoined(childId))
    }

    override fun onCleared() {
        super.onCleared()
        // SseManager는 종료하지 않음 (다른 곳에서도 사용)
        timerJob?.cancel()
    }
}
