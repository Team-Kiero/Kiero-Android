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

            if (childId != null) {
                fetchSchedule()
            } else {
                _state.value = UiState.Empty
            }
        }
    }

    fun fetchSchedule() {
        viewModelScope.launch {
            val childId = userInfoManager.getChildIdInfo() ?: return@launch
            val current = currentScheduleState ?: ParentScheduleState()

            if (_state.value is UiState.Success) {
                _state.updateSuccess { it.copy(isFetching = true) }
            }

            val monday = current.currentDate
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .toString()

            val sunday = current.currentDate
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                .toString()

            planRepository.getPlanAll(childId, monday, sunday)
                .onSuccess { model ->
                    _state.value = UiState.Success(
                        current.copy(
                            planAllModel = model,
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

    fun deleteSchedule(
        scheduleId: Long,
        selectedDate: String,
        isRecurring: Boolean,
        isIncludeFollowing: Boolean?,
    ) {
        val previousState = _state.value

        viewModelScope.launch {
            val safeDate = runCatching { LocalDate.parse(selectedDate.take(10)) }
                .getOrElse { LocalDate.now() }

            val selectedDateParam = if (!isRecurring) {
                safeDate.toString()
            } else {
                null
            }

            val startDateParam = if (isRecurring) {
                safeDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toString()
            } else {
                null
            }

            val endDateParam = if (isRecurring) {
                safeDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).toString()
            } else {
                null
            }

            planRepository.deleteSchedule(
                scheduleId = scheduleId,
                selectedDate = selectedDateParam,
                startDate = startDateParam,
                endDate = endDateParam,
                isIncludeFollowing = if (isRecurring) isIncludeFollowing else null
            ).onSuccess {
                when {
                    !isRecurring -> {
                        hideNormalSchedule(scheduleId, safeDate)
                    }

                    isIncludeFollowing == true -> {
                        hideRecurringSchedule(scheduleId)
                    }

                    else -> {
                        hideRecurringOccurrencesForThisWeek(scheduleId, safeDate)
                    }
                }

                _sideEffect.emit(ShowSnackBar("일정이 삭제되었습니다"))
            }.onFailure {
                _state.value = previousState
                _sideEffect.emit(ShowSnackBar("일정 삭제에 실패했습니다"))
            }
        }
    }

    private fun hideNormalSchedule(
        scheduleId: Long,
        selectedDate: LocalDate,
    ) {
        _state.updateSuccess { current ->
            current.copy(
                hiddenNormalScheduleKeys = current.hiddenNormalScheduleKeys +
                        ParentScheduleState.normalScheduleKey(scheduleId, selectedDate)
            )
        }
    }

    private fun hideRecurringSchedule(scheduleId: Long) {
        _state.updateSuccess { current ->
            current.copy(
                hiddenRecurringScheduleIds = current.hiddenRecurringScheduleIds + scheduleId
            )
        }
    }

    private fun hideRecurringOccurrencesForThisWeek(
        scheduleId: Long,
        selectedDate: LocalDate,
    ) {
        val currentUiState = _state.value as? UiState.Success ?: return
        val data = currentUiState.data
        val recurring = data.planAllModel?.recurringSchedules
            ?.find { it.scheduleId == scheduleId } ?: return

        val monday = selectedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val repeatStartDate = runCatching {
            LocalDate.parse(recurring.repeatStartDate.take(10))
        }.getOrNull() ?: LocalDate.MIN

        val startTime = parseScheduleStartTime(recurring.startTime) ?: return
        val now = LocalDateTime.now()

        val keysToHide = recurring.dayOfWeek
            .split(",")
            .map { it.trim().uppercase() }
            .mapNotNull { dayCode ->
                val dayIndex = dayCode.toDayIndexOrNull() ?: return@mapNotNull null
                val occurrenceDate = monday.plusDays(dayIndex.toLong())

                if (occurrenceDate.isBefore(repeatStartDate)) return@mapNotNull null

                val occurrenceDateTime = LocalDateTime.of(occurrenceDate, startTime)

                if (!occurrenceDateTime.isAfter(now)) return@mapNotNull null

                ParentScheduleState.recurringOccurrenceKey(scheduleId, occurrenceDate)
            }
            .toSet()

        _state.updateSuccess { current ->
            current.copy(
                hiddenRecurringOccurrenceKeys =
                    current.hiddenRecurringOccurrenceKeys + keysToHide
            )
        }
    }

    private fun parseScheduleStartTime(startTime: String): LocalTime? {
        return runCatching {
            val parts = startTime.split(":")
            LocalTime.of(parts[0].toInt(), parts[1].toInt())
        }.getOrNull()
    }

    private fun String.toDayIndexOrNull(): Int? {
        return when (this.uppercase()) {
            "MON" -> 0
            "TUE" -> 1
            "WED" -> 2
            "THU" -> 3
            "FRI" -> 4
            "SAT" -> 5
            "SUN" -> 6
            else -> null
        }
    }

    fun isScheduleEditable(schedule: ScheduleModel, targetDateStr: String?): Boolean {
        val today = LocalDate.now()
        val nowDateTime = LocalDateTime.now()

        val dateStr = when {
            !targetDateStr.isNullOrBlank() -> targetDateStr.take(10)
            schedule is NormalScheduleModel -> schedule.date.take(10)
            schedule is RecurringScheduleModel -> schedule.repeatStartDate.take(10)
            else -> return false
        }

        val scheduleDate = runCatching { LocalDate.parse(dateStr) }.getOrNull()
            ?: return false

        val scheduleDateTime = runCatching {
            val parts = schedule.startTime.split(":")
            LocalDateTime.of(
                scheduleDate,
                LocalTime.of(parts[0].toInt(), parts[1].toInt())
            )
        }.getOrNull() ?: return false

        // 1. 이미 시작 시간이 지났으면 무조건 수정/삭제 불가
        if (!scheduleDateTime.isAfter(nowDateTime)) return false

        val status = schedule.scheduleStatus?.uppercase()

        return when (schedule) {
            is NormalScheduleModel -> {
                if (status == "VERIFIED" || status == "COMPLETED") return false
                if (status == "SKIPPED" && scheduleDate == today) return false
                true
            }

            is RecurringScheduleModel -> {
                // 반복 일정은 클릭한 occurrence 날짜 기준으로 판단
                // 오늘 occurrence인 경우에만 완료/스킵 상태로 수정·삭제 제한
                if (scheduleDate.isEqual(today)) {
                    if (status == "VERIFIED" || status == "COMPLETED") return false
                    if (status == "SKIPPED") return false
                }
                true
            }

            else -> false
        }
    }

    fun resetToday() {
        val current = currentScheduleState ?: return
        val today = LocalDate.now()

        if (current.currentDate == today) return

        _state.value = UiState.Success(current.copy(currentDate = today))
        fetchSchedule()
    }

    fun onDateChange(isNext: Boolean) {
        val current = currentScheduleState ?: return
        val newDate =
            if (isNext) current.currentDate.plusWeeks(1) else current.currentDate.minusWeeks(1)
        val weeksDiff = ChronoUnit.WEEKS.between(LocalDate.now(), newDate)

        if (weeksDiff !in -12..12) return

        _state.value = UiState.Success(current.copy(currentDate = newDate))
        fetchSchedule()
    }

    private fun fetchParentInfo() {
        viewModelScope.launch {
            val info = userInfoManager.getParentInfo() ?: return@launch
            _authState.update { state ->
                state.copy(
                    parentInfo = state.parentInfo.copy(
                        parentName = info.name,
                        parentProfileImage = info.profileImage
                    )
                )
            }
        }
    }
}