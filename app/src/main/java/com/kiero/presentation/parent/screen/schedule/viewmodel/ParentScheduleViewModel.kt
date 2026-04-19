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
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
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
                authRepository.getChildren().onSuccess { children ->
                    if (children.isNotEmpty()) {
                        childId = children.first().childId
                        userInfoManager.saveChildIdInfo(childId!!)
                    }
                }
            }
            if (childId != null) fetchSchedule() else _state.value = UiState.Empty
        }
    }

    fun fetchSchedule() {
        viewModelScope.launch {
            val childId = userInfoManager.getChildIdInfo() ?: return@launch
            val s = currentScheduleState ?: ParentScheduleState()
            if (_state.value is UiState.Success) _state.updateSuccess { it.copy(isFetching = true) }

            val monday = s.currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toString()
            val sunday = s.currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).toString()

            planRepository.getPlanAll(childId, monday, sunday).onSuccess { model ->
                _state.value = UiState.Success(s.copy(planAllModel = model, isFireLit = model.isFireLit, isFetching = false))
            }.onFailure {
                _state.value = UiState.Failure(it.message ?: "데이터 로드 실패")
            }
        }
    }

    fun deleteSchedule(scheduleId: Long, selectedDate: String, isRecurring: Boolean, isIncludeFollowing: Boolean?) {
        val currentState = _state.value

        _state.update { currentUiState ->
            val data = (currentUiState as? UiState.Success)?.data ?: return@update currentUiState
            val currentPlan = data.planAllModel ?: return@update currentUiState
            val updatedPlan = currentPlan.copy(
                normalSchedules = currentPlan.normalSchedules.filterNot { it.scheduleId == scheduleId },
                recurringSchedules = currentPlan.recurringSchedules.filterNot { it.scheduleId == scheduleId }
            )
            UiState.Success(data.copy(planAllModel = updatedPlan))
        }

        viewModelScope.launch {
            val safeDate = runCatching { LocalDate.parse(selectedDate) }.getOrElse { LocalDate.now() }

            val selectedDateParam = if (!isRecurring) {
                if (selectedDate.isNotBlank()) selectedDate else safeDate.toString()
            } else null

            val startDateParam = if (isRecurring) {
                safeDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toString()
            } else null

            val endDateParam = if (isRecurring) {
                safeDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).toString()
            } else null

            planRepository.deleteSchedule(
                scheduleId = scheduleId,
                selectedDate = selectedDateParam,
                startDate = startDateParam,
                endDate = endDateParam,
                isIncludeFollowing = isIncludeFollowing
            ).onSuccess {
                _sideEffect.emit(ShowSnackBar("일정이 삭제되었습니다"))
            }.onFailure {
                _state.value = currentState // 실패 시 원복
                _sideEffect.emit(ShowSnackBar("일정 삭제에 실패했습니다"))
            }
        }
    }

    fun isScheduleEditable(schedule: ScheduleModel, targetDateStr: String?): Boolean {
        val today = LocalDate.now()
        val nowDateTime = LocalDateTime.now()

        val isFuture = runCatching {
            val dateStr = targetDateStr?.take(10) ?: when (schedule) {
                is NormalScheduleModel -> schedule.date.take(10)
                is RecurringScheduleModel -> schedule.repeatStartDate.take(10)
                else -> return false
            }
            val parsedDate = LocalDate.parse(dateStr)

            val timeParts = schedule.startTime.split(":")
            val parsedTime = LocalTime.of(timeParts[0].toInt(), timeParts[1].toInt())

            val scheduleDateTime = LocalDateTime.of(parsedDate, parsedTime)
            scheduleDateTime.isAfter(nowDateTime)
        }.getOrDefault(true)

        if (!isFuture) return false

        val status = schedule.scheduleStatus?.uppercase()

        if (status == "VERIFIED" || status == "COMPLETED") return false

        if (status == "SKIPPED") {
            val scheduleDateStr = targetDateStr?.take(10) ?: when (schedule) {
                is NormalScheduleModel -> schedule.date.take(10)
                is RecurringScheduleModel -> schedule.repeatStartDate.take(10)
                else -> null
            }
            val scheduleDate = scheduleDateStr?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
            if (scheduleDate == today) return false
        }

        return true
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

    private fun fetchParentInfo() {
        viewModelScope.launch {
            val info = userInfoManager.getParentInfo() ?: return@launch
            _authState.update { s ->
                s.copy(parentInfo = s.parentInfo.copy(parentName = info.name, parentProfileImage = info.profileImage))
            }
        }
    }
}