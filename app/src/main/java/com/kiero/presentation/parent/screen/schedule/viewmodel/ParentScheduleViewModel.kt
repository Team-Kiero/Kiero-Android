package com.kiero.presentation.parent.screen.schedule.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.common.extension.updateSuccess
import com.kiero.core.common.util.successData
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.core.model.UiState
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.data.parent.plan.model.NormalScheduleModel
import com.kiero.data.parent.plan.model.RecurringScheduleModel
import com.kiero.data.parent.plan.model.ScheduleModel
import com.kiero.data.parent.plan.repository.PlanRepository
import com.kiero.data.sse.manager.SseManager
import com.kiero.presentation.parent.screen.schedule.plan.state.ParentScheduleSideEffect
import com.kiero.presentation.parent.screen.schedule.plan.state.ParentScheduleSideEffect.ShowSnackBar
import com.kiero.presentation.parent.screen.schedule.plan.state.ParentScheduleState
import com.kiero.presentation.signup.parent.state.ParentSignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
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

    private val _authState = MutableStateFlow(ParentSignUpState())
    val authState = _authState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ParentScheduleSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private val currentScheduleState get() = _state.value.successData

    init {
        fetchParentInfo()
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

            if (childId != null) fetchSchedule()
            else _state.value = UiState.Empty
        }
    }

    fun fetchSchedule() {
        val today = LocalDate.now()

        viewModelScope.launch {
            val childId = userInfoManager.getChildIdInfo() ?: return@launch
            val s = currentScheduleState ?: ParentScheduleState()
            if (_state.value is UiState.Success) {
                _state.updateSuccess { it.copy(isFetching = true) }
            }

            val monday = s.currentDate.with(DayOfWeek.MONDAY).toString()
            val sunday = s.currentDate.with(DayOfWeek.SUNDAY).toString()

            planRepository.getPlanAll(childId, monday, sunday)
                .onSuccess { model ->
                    val todayDow = today.dayOfWeek.name.take(3)
                    val recurringSchedules = model.recurringSchedules.let { list ->
                        if (s.currentDate == today && model.isFireLit)
                            list.filterNot { it.dayOfWeek.contains(todayDow) }
                        else list
                    }

                    val newData = s.copy(
                        planAllModel = model.copy(recurringSchedules = recurringSchedules),
                        isFireLit    = model.isFireLit,
                        isFetching   = false,
                    )
                    _state.value = UiState.Success(newData)
                }
                .onFailure {
                    _state.value = UiState.Failure(it.message ?: "데이터 로드 실패")
                }
        }
    }

    fun resetToday() {
        val s = currentScheduleState ?: return
        val today = LocalDate.now()
        if (s.currentDate == today) return
        _state.value = UiState.Success(s.copy(currentDate = today))
        fetchSchedule()
    }

    fun onDateChange(isNext: Boolean) {
        val s = currentScheduleState ?: return
        val newDate = if (isNext) s.currentDate.plusWeeks(1) else s.currentDate.minusWeeks(1)
        val weeksDiff = ChronoUnit.WEEKS.between(LocalDate.now(), newDate)
        if (weeksDiff !in -12..12) return
        _state.value = UiState.Success(s.copy(currentDate = newDate))
        fetchSchedule()
    }

    fun deleteSchedule(scheduleId: Long, selectedDate: String, isIncludeFollowing: Boolean?) {
        viewModelScope.launch {
            planRepository.deleteSchedule(scheduleId, selectedDate, isIncludeFollowing)
                .onSuccess {
                    _sideEffect.emit(ShowSnackBar("일정이 삭제되었습니다"))

                    val currentState = currentScheduleState ?: return@onSuccess
                    val currentPlan = currentState.planAllModel ?: return@onSuccess

                    val newNormalSchedules = currentPlan.normalSchedules.filterNot { it.scheduleId == scheduleId }
                    val newRecurringSchedules = currentPlan.recurringSchedules.filterNot { it.scheduleId == scheduleId }

                    val updatedPlan = currentPlan.copy(
                        normalSchedules = newNormalSchedules,
                        recurringSchedules = newRecurringSchedules
                    )

                    _state.value = UiState.Success(currentState.copy(planAllModel = updatedPlan))
                }
        }
    }

    private fun fetchParentInfo() {
        viewModelScope.launch {
            val info = userInfoManager.getParentInfo() ?: return@launch
            _authState.update { s ->
                s.copy(
                    parentInfo = s.parentInfo.copy(
                        parentName         = info.name,
                        parentProfileImage = info.profileImage,
                    )
                )
            }
        }
    }

    fun isScheduleEditable(schedule: ScheduleModel): Boolean {
        val isFuture = runCatching {
            val dateStr = when (schedule) {
                is NormalScheduleModel    -> schedule.date
                is RecurringScheduleModel -> schedule.repeatStartDate
                else                     -> LocalDate.now().toString()
            }
            val scheduleDateTime = LocalDate.parse(dateStr)
                .atTime(LocalTime.parse(schedule.startTime.take(5), DateTimeFormatter.ofPattern("HH:mm")))

            scheduleDateTime.isAfter(LocalDateTime.now())
        }.getOrDefault(false)

        if (!isFuture) {
            return false
        }

        val hiddenStatuses = listOf("VERIFIED", "COMPLETED", "FAILED", "SKIPPED")
        if (schedule.scheduleStatus in hiddenStatuses) {
            return false
        }

        return true
    }
}