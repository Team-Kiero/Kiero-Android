package com.kiero.presentation.parent.schedule.plan.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.data.parent.plan.repository.PlanRepository
import com.kiero.presentation.parent.schedule.plan.model.ColorType
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
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class ParentPlanViewModel @Inject constructor(
    private val planRepository: PlanRepository,
    private val userInfoManager: UserInfoManager
) : ViewModel() {
    private val _state = MutableStateFlow(ParentPlanState())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ParentPlanSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    val textState = TextFieldState()

    init {
        fetchDefaultColor()
    }

    fun onCreatePlanClick() {
        viewModelScope.launch {
            val name = textState.text.toString().trim()
            val currentState = _state.value

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

            val selectedColor = currentState.selectedColorType
            val childId = userInfoManager.getChildIdInfo() ?: return@launch

            _state.update { it.copy(isLoading = true) }

            planRepository.postPlan(
                childId = childId,
                name = name,
                isRecurring = currentState.isRecurring,
                startTime = currentState.formatTimeForServer(currentState.startTime),
                endTime = currentState.formatTimeForServer(currentState.endTime),
                scheduleColor = selectedColor.name,
                dayOfWeek = if (currentState.isRecurring) currentState.formattedDays else null,
                date = if (!currentState.isRecurring) currentState.selectedDate else null
            ).onSuccess {
                _sideEffect.emit(ParentPlanSideEffect.ShowSnackBar("일정이 등록되었습니다"))
                kotlinx.coroutines.delay(200)
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
            currentState.copy(selectedDays = newDays)
        }
    }

    fun onDayClick(dayIndex: Int) {
        _state.update { currentState ->
            val newDays = if (currentState.selectedDays.contains(dayIndex)) {
                currentState.selectedDays - dayIndex
            } else {
                currentState.selectedDays + dayIndex
            }

            if (!currentState.isRecurring) {
                val monday =
                    currentState.currentReferenceDate.with(
                        TemporalAdjusters.previousOrSame(
                            DayOfWeek.MONDAY
                        )
                    )
                val actualDate = monday.plusDays(dayIndex.toLong()).toString()
                currentState.copy(selectedDays = newDays, selectedDate = actualDate)
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
        _state.update { if (isStart) it.copy(startTime = time) else it.copy(endTime = time) }
    }

}