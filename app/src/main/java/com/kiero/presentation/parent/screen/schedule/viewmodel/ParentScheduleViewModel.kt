package com.kiero.presentation.parent.screen.schedule.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.core.model.UiState
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.data.parent.plan.repository.PlanRepository
import com.kiero.data.sse.manager.SseManager
import com.kiero.presentation.parent.screen.schedule.plan.state.ParentScheduleSideEffect
import com.kiero.presentation.parent.screen.schedule.plan.state.ParentScheduleState
import com.kiero.presentation.signup.parent.state.ParentSignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class ParentScheduleViewModel @Inject constructor(
    private val planRepository: PlanRepository,
    private val authRepository: AuthRepository,
    private val userInfoManager: UserInfoManager,
    private val sseManager: SseManager,
) : ViewModel() {
    private val _state = MutableStateFlow<UiState<ParentScheduleState>>(UiState.Loading)
    val state = _state.asStateFlow()

    private val _authstate = MutableStateFlow(ParentSignUpState())
    val authstate: StateFlow<ParentSignUpState> = _authstate.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ParentScheduleSideEffect>()
    val sideEffect: SharedFlow<ParentScheduleSideEffect> = _sideEffect.asSharedFlow()

    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex.asStateFlow()

    private val _childId = MutableStateFlow<Long?>(null)
    val childId: StateFlow<Long?> = _childId.asStateFlow()

    init {
        initFetchParentInfo()
        sseManager.startParentSubscription()
    }

    fun ensureChildIdAndStartSse() {
        viewModelScope.launch {
            var childId = userInfoManager.getChildIdInfo()

            if (childId == null) {
                Timber.d("📌 childId 없음 - 자녀 목록 조회")
                authRepository.getChildren()
                    .onSuccess { children ->
                        if (children.isNotEmpty()) {
                            childId = children.first().childId
                            userInfoManager.saveChildIdInfo(childId!!)
                            Timber.d("📌 childId 저장 완료: $childId")
                        }
                    }
                    .onFailure {
                        Timber.e(it, "📌 자녀 목록 조회 실패")
                    }
            }

            _childId.value = childId

            if (childId != null) {
                fetchSchedule()
            } else {
                _state.value = UiState.Empty  // childId 없으면 Empty로
            }

            Timber.d("📢 SSE 구독 시작")
            sseManager.startParentSubscription()
        }
    }

    fun fetchSchedule() {
        val currentState = (_state.value as? UiState.Success)?.data ?: ParentScheduleState()
        val today = LocalDate.now()
        val monday = currentState.currentDate.with(DayOfWeek.MONDAY).toString()
        val sunday = currentState.currentDate.with(DayOfWeek.SUNDAY).toString()

        viewModelScope.launch {
            val childId: Long = userInfoManager.getChildIdInfo() ?: return@launch

            _state.value = UiState.Success(currentState.copy(isFetching = true))
            planRepository.getPlanAll(childId, monday, sunday)
                .onSuccess { model ->
                    val todayDayOfWeek = today.dayOfWeek.name.take(3).uppercase()


                    val finalRecurringSchedules =
                        if (currentState.currentDate == today && model.isFireLit) {
                            model.recurringSchedules.filterNot {
                                it.dayOfWeek.contains(
                                    todayDayOfWeek
                                )
                            }
                        } else {
                            model.recurringSchedules
                        }
                    _state.value = UiState.Success(
                        currentState.copy(
                            planAllModel = model.copy(recurringSchedules = finalRecurringSchedules),
                            isFireLit = model.isFireLit,
                            isFetching = false
                        )
                    )
                }
                .onFailure {
                    _state.value = UiState.Failure(it.message ?: "데이터 로드 실패")
                }
        }
    }

    fun resetToday() {
        val currentState = (_state.value as? UiState.Success)?.data ?: return
        val today = LocalDate.now()

        if (currentState.currentDate == today) return

        _state.value = UiState.Success(currentState.copy(currentDate = today))

    }

    fun initFetchParentInfo() {
        viewModelScope.launch {
            val parentInfo = userInfoManager.getParentInfo() ?: return@launch  // ← !! 제거
            _authstate.update { currentState ->
                currentState.copy(
                    parentInfo = currentState.parentInfo.copy(
                        parentName = parentInfo.name,
                        parentProfileImage = parentInfo.profileImage
                    )
                )
            }
        }
    }

    fun onDateChange(isNext: Boolean) {
        val currentState = (_state.value as? UiState.Success)?.data ?: return
        val today = LocalDate.now()
        val newDate =
            if (isNext) currentState.currentDate.plusWeeks(1) else currentState.currentDate.minusWeeks(
                1
            )

        val weeksDiff = ChronoUnit.WEEKS.between(today, newDate)
        if (weeksDiff in -12..12) {
            _state.value = UiState.Success(currentState.copy(currentDate = newDate))
            fetchSchedule()
        } else {
            //Todo: Icon 색상 및 막기 처리
        }
        _state.value = UiState.Success(currentState.copy(currentDate = newDate))

        viewModelScope.launch {
            if (userInfoManager.getChildIdInfo() != null) {
                fetchSchedule()
            }
        }
    }
}

