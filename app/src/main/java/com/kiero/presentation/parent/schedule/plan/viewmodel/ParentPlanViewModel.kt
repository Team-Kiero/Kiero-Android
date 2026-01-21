package com.kiero.presentation.parent.schedule.plan.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.data.parent.plan.repository.PlanRepository
import com.kiero.presentation.parent.schedule.plan.model.ColorType
import com.kiero.presentation.parent.schedule.plan.navigation.ScheduleAdd
import com.kiero.presentation.parent.schedule.plan.state.ParentPlanSideEffect
import com.kiero.presentation.parent.schedule.plan.state.ParentPlanState
import com.kiero.presentation.parent.schedule.plan.state.parseLocalTime
import com.kiero.presentation.parent.schedule.plan.state.validateAndTimeAdjustment
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

            if (name.isBlank() && currentState.selectedDays.isEmpty()) {
                _sideEffect.emit(ParentPlanSideEffect.ShowSnackBar("일정 저장에 실패했어요. 잠시 후 다시 시도해주세요"))
            }

            if (name.isBlank()) {
                _sideEffect.emit(ParentPlanSideEffect.ShowSnackBar("일정 이름을 입력해주세요"))
                return@launch
            }
            when {
                currentState.isRecurring -> {
                    if (currentState.selectedDays.isEmpty()) {
                        _sideEffect.emit(ParentPlanSideEffect.ShowSnackBar("요일을 선택해주세요"))
                        return@launch
                    }
                }

                else -> {
                    if (currentState.selectedDays.isEmpty()) {
                        _sideEffect.emit(ParentPlanSideEffect.ShowSnackBar("날짜를 선택해주세요"))
                        return@launch
                    }
                    if (currentState.selectedDate.isBlank()) {
                        _sideEffect.emit(ParentPlanSideEffect.ShowSnackBar("날짜를 선택해주세요"))
                        return@launch
                    }
                }
            }

            if (!currentState.isTimeValid(currentState.startTime, currentState.endTime)) {
                _sideEffect.emit(ParentPlanSideEffect.ShowSnackBar("종료 시간은 시작 시간보다 늦어야 합니다"))
                return@launch
            }
            if (!currentState.isRecurring) {
                if (currentState.selectedDate.contains(today.toString())) {
                    _sideEffect.emit(ParentPlanSideEffect.ShowSnackBar("일정이 등록되었어요. (오늘일정은 마감되어 다음부터 적용돼요!)"))
                    kotlinx.coroutines.delay(500)
                    _sideEffect.emit(ParentPlanSideEffect.navigateUp)
                    return@launch
                }
            }
            if (!currentState.isRecurring && currentState.selectedDate.contains(today.toString())) {
                val selectedStartTime = parseLocalTime(currentState.startTime)


                if (selectedStartTime.isBefore(currentTime) || currentState.isFireLit) {
                    _sideEffect.emit(ParentPlanSideEffect.ShowSnackBar("일정이 등록되었어요. (오늘일정은 마감되어 다음부터 적용돼요!)"))
                    kotlinx.coroutines.delay(500)
                    _sideEffect.emit(ParentPlanSideEffect.navigateUp)
                    return@launch
                }
            }


            _state.update { it.copy(isLoading = true) }

            val selectedColor = currentState.selectedColorType
            val childId = userInfoManager.getChildIdInfo() ?: run {
                _state.update { it.copy(isLoading = false) }
                return@launch
            }


            planRepository.postPlan(
                childId = childId,
                name = name,
                isRecurring = currentState.isRecurring,
                startTime = currentState.formatTimeForServer(currentState.startTime),
                endTime = currentState.formatTimeForServer(currentState.endTime),
                scheduleColor = selectedColor.name,
                dayOfWeek = currentState.formattedDays,
                dates = if (!currentState.isRecurring) currentState.selectedDate else null
            ).onSuccess {
                _sideEffect.emit(ParentPlanSideEffect.ShowSnackBar("일정이 등록되었습니다"))
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
            // 1. 새로운 요일 Set 생성 (토글 로직)
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
            when {
                newIsRecurring -> {
                    currentState.copy(
                        isRecurring = true,
                        selectedDate = ""
                    )
                }

                else -> {
                    currentState.copy(
                        isRecurring = false,
                        selectedDays = emptySet(),
                        selectedDate = ""
                    )
                }
            }
        }
    }

    fun onTimeSelected(isStart: Boolean, time: String) {
        viewModelScope.launch {
            val result = validateAndTimeAdjustment(time)

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