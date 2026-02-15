package com.kiero.presentation.parent.schedule.plan.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.domain.repository.parent.plan.PlanRepository
import com.kiero.presentation.parent.schedule.plan.model.ColorType
import com.kiero.presentation.parent.schedule.plan.navigation.ScheduleAdd
import com.kiero.presentation.parent.schedule.plan.state.ParentPlanSideEffect
import com.kiero.presentation.parent.schedule.plan.state.ParentPlanState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class ParentPlanViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val planRepository: PlanRepository,
    private val userInfoManager: UserInfoManager,
) : ViewModel() {
    private val args = savedStateHandle.toRoute<ScheduleAdd>()

    private val _state = MutableStateFlow(
        ParentPlanState(
            currentReferenceDate = LocalDate.parse(args.initialDate),
            isFireLit = args.isFireLit
        )
    )
    val state = _state.asStateFlow()
    private val _sideEffect = MutableSharedFlow<ParentPlanSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    val textState = TextFieldState()

    init {
        fetchDefaultColor()
    }

    fun onCreatePlanClick() {
        if (_state.value.isLoading) return

        viewModelScope.launch {
            val name = textState.text.toString().trim()
            val currentState = _state.value
            val today = LocalDate.now()
            val currentTime = LocalTime.now()

            val isNameMissing = name.isBlank()
            val isDaysMissing = currentState.selectedDays.isEmpty()
            val isTimeMissing = currentState.startTime == null || currentState.endTime == null

            val missingCount = listOf(isNameMissing, isDaysMissing, isTimeMissing).count { it }

            when {
                missingCount >= 2 -> {
                    _sideEffect.emit(ParentPlanSideEffect.ShowSnackBar("일정 저장에 실패했어요. 잠시 후 다시 시도해주세요"))
                    return@launch
                }

                isNameMissing -> {
                    _sideEffect.emit(ParentPlanSideEffect.ShowSnackBar("일정 이름을 입력해주세요"))
                    return@launch
                }

                isDaysMissing -> {
                    _sideEffect.emit(ParentPlanSideEffect.ShowSnackBar("요일을 선택해주세요"))
                    return@launch
                }

                isTimeMissing -> {
                    _sideEffect.emit(ParentPlanSideEffect.ShowSnackBar("시간을 선택해주세요"))
                    return@launch
                }
            }

            if (!currentState.isTimeValid) {
                _sideEffect.emit(ParentPlanSideEffect.ShowSnackBar("종료 시간은 시작 시간보다 늦어야 합니다"))
                return@launch
            }
            val isTodayIncluded = if (currentState.isRecurring) {
                val todayDayOfWeekIndex = (today.dayOfWeek.value - 1)
                currentState.selectedDays.contains(todayDayOfWeekIndex)
            } else {
                currentState.selectedDate.contains(today.toString())
            }

            val isAlreadyClosedToday = if (isTodayIncluded) {
                val selectedStartTime = currentState.parseLocalTime(currentState.displayStartTime)
                selectedStartTime.isBefore(currentTime) || currentState.isFireLit
            } else false



            if (!currentState.isRecurring && isAlreadyClosedToday) {
                _sideEffect.emit(ParentPlanSideEffect.ShowSnackBar("일정이 등록되었어요. (오늘 일정은 마감되어 다음부터 적용돼요!)"))
                kotlinx.coroutines.delay(500)
                _sideEffect.emit(ParentPlanSideEffect.navigateUp)
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            val selectedColor = currentState.selectedColorType
            val childId = userInfoManager.getChildIdInfo() ?: run {
                _state.update { it.copy(isLoading = false) }
                return@launch
            }
            val finalDateParam = if (currentState.isRecurring) {
                if (isAlreadyClosedToday) today.plusDays(1).toString() else today.toString()
            } else {
                currentState.selectedDate
            }

            planRepository.postPlan(
                childId = childId,
                name = name,
                isRecurring = currentState.isRecurring,
                startTime = currentState.formatTimeForServer(currentState.startTime),
                endTime = currentState.formatTimeForServer(currentState.endTime),
                scheduleColor = selectedColor.name,
                dayOfWeek = if (currentState.isRecurring) currentState.formattedDays else null,
                dates = if (currentState.isRecurring) null else currentState.selectedDate
            ).onSuccess {
                if (isAlreadyClosedToday) {
                    _sideEffect.emit(ParentPlanSideEffect.ShowSnackBar("일정이 등록되었어요. (오늘 일정은 마감되어 다음부터 적용돼요!)"))
                } else {
                    _sideEffect.emit(ParentPlanSideEffect.ShowSnackBar("일정이 등록되었습니다"))
                }
                kotlinx.coroutines.delay(200)
                _sideEffect.emit(ParentPlanSideEffect.navigateUp)
            }.onFailure { error ->
                _sideEffect.emit(ParentPlanSideEffect.ShowSnackBar("해당 시간에 이미 등록된 일정이 있습니다"))
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun fetchDefaultColor() {
        viewModelScope.launch {
            val childId = userInfoManager.getChildIdInfo() ?: return@launch

            planRepository.getPlanColor(childId)
                .onSuccess { result ->
                    val colorType = ColorType.entries.find { it.name == result.scheduleColor }
                        ?: ColorType.SCHEDULE1
                    _state.update { it.copy(selectedColorType = colorType) }
                }
        }
    }

    fun onAllDaysSelect(isCurrentlyAllSelected: Boolean) {
        _state.update { currentState ->
            val newDays = if (isCurrentlyAllSelected) emptySet() else (0..6).toSet()

            if (!currentState.isRecurring) {
                val datesString = calculateSelectedDates(currentState.currentReferenceDate, newDays)
                currentState.copy(selectedDays = newDays, selectedDate = datesString)
            } else {
                currentState.copy(selectedDays = newDays)
            }
        }
    }


    private fun calculateSelectedDates(
        currentReferenceDate: LocalDate,
        selectedDays: Set<Int>,
    ): String {
        val monday = currentReferenceDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        return selectedDays.sorted()
            .map { dayIndex -> monday.plusDays(dayIndex.toLong()).toString() }
            .joinToString(", ")
    }

    fun onDayClick(dayIndex: Int) {
        _state.update { currentState ->
            val newDays = if (currentState.selectedDays.contains(dayIndex)) {
                currentState.selectedDays - dayIndex
            } else {
                currentState.selectedDays + dayIndex
            }

            if (!currentState.isRecurring) {
                val datesString = calculateSelectedDates(currentState.currentReferenceDate, newDays)
                currentState.copy(
                    selectedDays = newDays,
                    selectedDate = datesString
                )
            } else {
                currentState.copy(selectedDays = newDays)
            }
        }
    }

    fun onColorSelected(colorType: ColorType) {
        _state.update {
            val newState = it.copy(selectedColorType = colorType, showColorPicker = false)
            newState
        }
    }

    fun toggleColorPicker(isVisible: Boolean) {
        _state.update { it.copy(showColorPicker = isVisible) }
    }

    fun onPreviousWeek() {
        _state.update { currentState ->
            val startOfThisWeek =
                LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            if (currentState.currentReferenceDate.isAfter(startOfThisWeek)) {
                currentState.copy(
                    currentReferenceDate = currentState.currentReferenceDate.minusWeeks(
                        1
                    )
                )
            } else currentState
        }
    }

    fun onNextWeek() {
        _state.update { currentState ->
            val nextWeek = currentState.currentReferenceDate.plusWeeks(1)
            currentState.copy(currentReferenceDate = nextWeek)
        }
    }

    fun onRecurringToggle() {
        _state.update { currentState ->
            val newIsRecurring = !currentState.isRecurring

            if (newIsRecurring) {
                currentState.copy(
                    isRecurring = true,
                    selectedDate = ""
                )
            } else {
                val updatedDates = calculateSelectedDates(
                    currentState.currentReferenceDate,
                    currentState.selectedDays
                )

                currentState.copy(
                    isRecurring = false,
                    selectedDate = updatedDates
                )
            }
        }
    }

    fun onTimeSelected(isStart: Boolean, time: String) {
        viewModelScope.launch {
            val result = _state.value.validateAndTimeAdjustment(time)

            _state.update {
                if (isStart) it.copy(startTime = result.adjustedTime)
                else it.copy(endTime = result.adjustedTime)
            }

            if (!result.isValid) {
                _sideEffect.emit(ParentPlanSideEffect.ShowSnackBar(result.message ?: ""))
            }
        }
    }
}