package com.kiero.presentation.parent.screen.schedule.plan.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.data.parent.plan.repository.PlanRepository
import com.kiero.presentation.parent.screen.schedule.plan.model.ColorType
import com.kiero.presentation.parent.screen.schedule.plan.navigation.ScheduleAdd
import com.kiero.presentation.parent.screen.schedule.plan.navigation.ScheduleEdit
import com.kiero.presentation.parent.screen.schedule.plan.state.ParentPlanSideEffect
import com.kiero.presentation.parent.screen.schedule.plan.state.ParentPlanSideEffect.ShowSnackBar
import com.kiero.presentation.parent.screen.schedule.plan.state.ParentPlanSideEffect.navigateUp
import com.kiero.presentation.parent.screen.schedule.plan.state.ParentPlanState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    private val addArgs = runCatching { savedStateHandle.toRoute<ScheduleAdd>() }.getOrNull()
    private val editArgs = runCatching { savedStateHandle.toRoute<ScheduleEdit>() }.getOrNull()

    val isEditMode = editArgs != null
    val isEditRecurring = editArgs?.isRecurring ?: false

    private val _state = MutableStateFlow(
        ParentPlanState(
            currentReferenceDate = LocalDate.parse(
                editArgs?.selectedDate ?: addArgs?.initialDate ?: LocalDate.now().toString()
            ),
            isFireLit = addArgs?.isFireLit ?: false,
        )
    )
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ParentPlanSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    val textState = TextFieldState()

    init {
        if (isEditMode) initEditMode() else fetchDefaultColor()
    }

    private fun initEditMode() {
        val args = editArgs ?: return
        val state = _state.value

        val selectedDays = when {
            args.isRecurring -> args.dayOfWeek.toDayIndices()
            else -> args.dates
                ?.trim()
                ?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
                ?.let { setOf(it.dayOfWeek.value - 1) }
                ?: emptySet()
        }

        textState.edit { replace(0, length, args.name) }

        _state.update {
            it.copy(
                isRecurring       = args.isRecurring,
                startTime         = state.serverTimeToDisplayTime(args.startTime),
                endTime           = state.serverTimeToDisplayTime(args.endTime),
                selectedColorType = ColorType.fromHexCode(args.scheduleColor),
                selectedDays      = selectedDays,
                selectedDate      = args.dates.orEmpty(),
            )
        }
    }

    fun onCreatePlanClick(isIncludeFollowing: Boolean? = null) {
        if (isEditMode) onUpdatePlanClick(isIncludeFollowing) else onAddPlanClick()
    }

    private fun onAddPlanClick() {
        if (_state.value.isLoading) return

        viewModelScope.launch {
            val name = textState.text.toString().trim()
            val s = _state.value

            validateInputs(name, s)?.let { error ->
                _sideEffect.emit(ShowSnackBar(error))
                return@launch
            }

            if (!s.isTimeValid) {
                _sideEffect.emit(ShowSnackBar("종료 시간은 시작 시간보다 늦어야 합니다"))
                return@launch
            }

            val today = LocalDate.now()
            val now = LocalTime.now()

            if (!s.isRecurring) {
                val selectedDates = s.selectedDate
                    .split(",")
                    .mapNotNull { dateStr -> runCatching { LocalDate.parse(dateStr.trim()) }.getOrNull() }

                val hasPastDate = selectedDates.any { it.isBefore(today) }
                if (hasPastDate) {
                    _sideEffect.emit(ShowSnackBar("과거 날짜에는 일정을 등록할 수 없습니다"))
                    return@launch
                }

                val isTodayOnly = selectedDates.size == 1 && selectedDates.first() == today
                if (isTodayOnly) {
                    val startTime = s.startTime?.let {
                        runCatching {
                            LocalTime.parse(it.take(5), java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
                        }.getOrNull()
                    }

                    if (startTime != null && startTime.isBefore(now)) {
                        _sideEffect.emit(ShowSnackBar("이미 지난 시간에는 일정을 등록할 수 없어요"))
                        return@launch
                    }

                    if (s.isFireLit) {
                        _sideEffect.emit(ShowSnackBar("오늘 일정이 마감되어, 일정을 추가할 수 없어요"))
                        return@launch
                    }
                }
            }

            val isTodayIncluded = s.isTodayIncluded(today)
            val isClosedToday = isTodayIncluded &&
                    (s.parseLocalTime(s.displayStartTime).isBefore(now) || s.isFireLit)

            if (!s.isRecurring && isClosedToday) {
                _sideEffect.emit(ShowSnackBar("일정이 등록되었어요. (오늘 일정은 마감되어 다음부터 적용돼요!)"))
                delay(500)
                _sideEffect.emit(navigateUp)
                return@launch
            }

            val childId = getChildIdOrReturn() ?: return@launch

            planRepository.postPlan(
                childId       = childId,
                name          = name,
                isRecurring   = s.isRecurring,
                startTime     = s.formatTimeForServer(s.startTime),
                endTime       = s.formatTimeForServer(s.endTime),
                scheduleColor = s.selectedColorType.name,
                dayOfWeek     = s.formattedDays.takeIf { s.isRecurring },
                dates         = s.selectedDate.takeUnless { s.isRecurring },
            ).onSuccess {
                val msg = if (isClosedToday) "일정이 등록되었어요. (오늘 일정은 마감되어 다음부터 적용돼요!)"
                else "일정이 등록되었습니다"
                _sideEffect.emit(ShowSnackBar(msg))
                delay(200)
                _sideEffect.emit(navigateUp)
            }.onFailure {
                _sideEffect.emit(ShowSnackBar("해당 시간에 이미 등록된 일정이 있습니다"))
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun onUpdatePlanClick(isIncludeFollowing: Boolean?) {
        if (_state.value.isLoading) return
        val scheduleId   = editArgs?.scheduleId ?: return
        val selectedDate = editArgs?.selectedDate ?: return

        viewModelScope.launch {
            val name = textState.text.toString().trim()
            val s = _state.value

            validateInputs(name, s)?.let { error ->
                _sideEffect.emit(ShowSnackBar(error))
                return@launch
            }

            if (!s.isTimeValid) {
                _sideEffect.emit(ShowSnackBar("종료 시간은 시작 시간보다 늦어야 합니다"))
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            planRepository.updateSchedule(
                scheduleId         = scheduleId,
                selectedDate       = selectedDate,
                name               = name,
                isRecurring        = s.isRecurring,
                startTime          = s.formatTimeForServer(s.startTime),
                endTime            = s.formatTimeForServer(s.endTime),
                scheduleColor      = s.selectedColorType.name,
                dayOfWeek          = s.formattedDays.takeIf { s.isRecurring },
                dates              = s.selectedDate.takeUnless { s.isRecurring },
                isIncludeFollowing = isIncludeFollowing,
            ).onSuccess {
                _sideEffect.emit(ShowSnackBar("일정이 수정되었습니다"))
                delay(200)
                _sideEffect.emit(navigateUp)
            }.onFailure {
                _sideEffect.emit(ShowSnackBar("일정 수정에 실패했습니다"))
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun validateInputs(name: String, state: ParentPlanState): String? {
        val missing = listOf(
            name.isBlank(),
            state.selectedDays.isEmpty(),
            state.startTime == null || state.endTime == null,
        ).count { it }

        return when {
            missing >= 2           -> "일정 저장에 실패했어요. 잠시 후 다시 시도해주세요"
            name.isBlank()         -> "일정 이름을 입력해주세요"
            state.selectedDays.isEmpty() -> "요일을 선택해주세요"
            state.startTime == null || state.endTime == null -> "시간을 선택해주세요"
            else                   -> null
        }
    }

    private suspend fun getChildIdOrReturn(): Long? {
        val childId = userInfoManager.getChildIdInfo()
        if (childId == null) _state.update { it.copy(isLoading = false) }
        return childId
    }

    fun fetchDefaultColor() {
        viewModelScope.launch {
            val childId = userInfoManager.getChildIdInfo() ?: return@launch
            planRepository.getPlanColor(childId).onSuccess { result ->
                _state.update { it.copy(selectedColorType = ColorType.fromHexCode(result.colorCode)) }
            }
        }
    }

    fun onDayClick(dayIndex: Int) {
        _state.update { s ->
            val newDays = if (dayIndex in s.selectedDays) s.selectedDays - dayIndex
            else s.selectedDays + dayIndex
            s.withUpdatedDays(newDays)
        }
    }

    fun onAllDaysSelect(isCurrentlyAllSelected: Boolean) {
        _state.update { s ->
            val newDays = if (isCurrentlyAllSelected) emptySet() else (0..6).toSet()
            s.withUpdatedDays(newDays)
        }
    }

    fun onRecurringToggle() {
        _state.update { s ->
            if (s.isRecurring) {
                s.copy(
                    isRecurring  = false,
                    selectedDate = s.selectedDays.toDateString(s.currentReferenceDate),
                )
            } else {
                s.copy(isRecurring = true, selectedDate = "")
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
                _sideEffect.emit(ShowSnackBar(result.message.orEmpty()))
            }
        }
    }

    fun onColorSelected(colorType: ColorType) {
        _state.update { it.copy(selectedColorType = colorType, showColorPicker = false) }
    }

    fun toggleColorPicker(show: Boolean) {
        _state.update { it.copy(showColorPicker = show) }
    }

    fun onPreviousWeek() {
        _state.update { s ->
            val thisMonday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            if (s.currentReferenceDate.isAfter(thisMonday))
                s.copy(currentReferenceDate = s.currentReferenceDate.minusWeeks(1))
            else s
        }
    }

    fun onNextWeek() {
        _state.update { it.copy(currentReferenceDate = it.currentReferenceDate.plusWeeks(1)) }
    }

    private fun ParentPlanState.isTodayIncluded(today: LocalDate): Boolean =
        if (isRecurring) selectedDays.contains(today.dayOfWeek.value - 1)
        else selectedDate.contains(today.toString())

    private fun ParentPlanState.withUpdatedDays(newDays: Set<Int>): ParentPlanState =
        if (isRecurring) copy(selectedDays = newDays)
        else copy(selectedDays = newDays, selectedDate = newDays.toDateString(currentReferenceDate))

    private fun Set<Int>.toDateString(referenceDate: LocalDate): String {
        val monday = referenceDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        return sorted().joinToString(", ") { monday.plusDays(it.toLong()).toString() }
    }

    private fun String?.toDayIndices(): Set<Int> {
        if (isNullOrBlank()) return emptySet()
        val map = mapOf("MON" to 0, "TUE" to 1, "WED" to 2, "THU" to 3, "FRI" to 4, "SAT" to 5, "SUN" to 6)
        return split(",").mapNotNull { map[it.trim()] }.toSet()
    }
}