package com.kiero.presentation.signup.parent.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kiero.core.common.extension.toHandleErrorMessage
import com.kiero.core.common.util.formatTime
import com.kiero.core.common.viewmodel.throttleFirst
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.data.demo.repository.DemoRepository
import com.kiero.data.parent.signup.repository.ParentSignUpRepository
import com.kiero.presentation.signup.parent.model.ParentSignUpStep
import com.kiero.presentation.signup.parent.model.toUiModel
import com.kiero.presentation.signup.parent.navigation.ParentSignUp
import com.kiero.presentation.signup.parent.state.ParentSignUpSideEffect
import com.kiero.presentation.signup.parent.state.ParentSignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
    private val demoRepository: DemoRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ParentSignUpState())
    val state: StateFlow<ParentSignUpState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ParentSignUpSideEffect>()
    val sideEffect: SharedFlow<ParentSignUpSideEffect> = _sideEffect.asSharedFlow()

    private val parentInfo = savedStateHandle.toRoute<ParentSignUp>()

    private var timerJob: Job? = null
    private var copyJob: Job? = null
    private val TIMER_DURATION_SECONDS = 10 * 1

    init {
        initFetchParentInfo(
            parentName = parentInfo.parentName,
            parentProfileImage = parentInfo.parentProfileImage
        )
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

                    demoRepository.postDemo()
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
        parentProfileImage: String
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

    fun logOut() {
        viewModelScope.launch {
            authRepository.postLogout()
            demoRepository.deleteDemo()

            _sideEffect.emit(ParentSignUpSideEffect.NavigateToSelection)
            _state.update {
                it.copy(
                    isLoading = false
                )
            }
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
        _state.update {
            it.copy(
                currentStep = ParentSignUpStep.ADDCHILD
            )
        }
    }
}