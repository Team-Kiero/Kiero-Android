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
                editArgs?.selectedDate?.takeIf { it.isNotBlank() }
                    ?: addArgs?.initialDate?.takeIf { it.isNotBlank() }
                    ?: LocalDate.now().toString()
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
                isRecurring = args.isRecurring,
                startTime = state.serverTimeToDisplayTime(args.startTime),
                endTime = state.serverTimeToDisplayTime(args.endTime),
                selectedColorType = ColorType.fromHexCode(args.scheduleColor),
                selectedDays = selectedDays,
                selectedDate = args.dates.orEmpty(),
            )
        }
    }

    fun shouldShowEditDialog(): Boolean {
        if (!isEditMode) return false
        val s = state.value
        val originalIsRecurring = editArgs?.isRecurring ?: false
        val currentIsRecurring = s.isRecurring
        val originalDays = editArgs?.dayOfWeek.toDayIndices() ?: emptySet()
        val currentDays = s.selectedDays

        return originalIsRecurring && currentIsRecurring && (originalDays == currentDays)
    }

    private fun validatePlanInputs(name: String, s: ParentPlanState, today: LocalDate, now: LocalTime): List<String> {
        val errors = mutableListOf<String>()

        if (name.isBlank()) errors.add("일정 이름을 입력해주세요.")
        if (s.selectedDays.isEmpty()) errors.add("요일을 선택해주세요.")

        if (s.startTime == null || s.endTime == null) {
            errors.add("시간을 선택해주세요.")
        } else if (!s.isTimeValid) {
            errors.add("종료시간은 시작시간보다 늦어야 합니다.")
        }

        if (!s.isRecurring) {
            val monday = s.currentReferenceDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            val selectedDates = s.selectedDays.map { monday.plusDays(it.toLong()) }

            if (selectedDates.any { it.isBefore(today) }) {
                errors.add("과거 날짜에는 일정을 등록할 수 없습니다.")
            }

            if (selectedDates.contains(today)) {
                val startTime = runCatching { s.parseLocalTime(s.displayStartTime) }.getOrNull()
                if (startTime != null && startTime.isBefore(now)) {
                    errors.add("이미 지난 시간에는 일정을 등록할 수 없어요.")
                }
                if (s.isFireLit) {
                    errors.add("오늘 일정이 마감되어, 일정을 추가할 수 없어요.")
                }
            }
        }
        return errors
    }

    fun onCreatePlanClick(isIncludeFollowing: Boolean? = null) {
        if (isEditMode) onUpdatePlanClick(isIncludeFollowing) else onAddPlanClick()
    }

    private fun onAddPlanClick() {
        if (_state.value.isLoading) return
        viewModelScope.launch {
            val name = textState.text.toString().trim()
            val s = _state.value
            val today = LocalDate.now()
            val now = LocalTime.now()

            val errors = validatePlanInputs(name, s, today, now)
            if (errors.isNotEmpty()) {
                val errorMsg = if (errors.size >= 2) "일정 저장에 실패했어요." else errors.first()
                _sideEffect.emit(ShowSnackBar(errorMsg))
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            val isCurrentWeek = !s.currentReferenceDate.with(DayOfWeek.MONDAY).isAfter(today.with(DayOfWeek.MONDAY))
            val isTodaySelected = s.selectedDays.contains(today.dayOfWeek.value - 1) && isCurrentWeek
            val startTime = runCatching { s.parseLocalTime(s.displayStartTime) }.getOrNull()
            val isPastTimeToday = startTime != null && startTime.isBefore(now)

            val isCaseB = isTodaySelected && (isPastTimeToday || s.isFireLit)

            val childId = getChildIdOrReturn() ?: return@launch

            val firstOrderDateStr = if (s.isRecurring) {
                var nextValidDate: LocalDate? = null
                val searchStartDate = if (isCurrentWeek) today else s.currentReferenceDate.with(DayOfWeek.MONDAY)

                for (i in 0L..13L) {
                    val dateToCheck = searchStartDate.plusDays(i)

                    if (dateToCheck.isBefore(today)) continue
                    if (dateToCheck == today && isCaseB) continue

                    if (s.selectedDays.contains(dateToCheck.dayOfWeek.value - 1)) {
                        nextValidDate = dateToCheck
                        break
                    }
                }
                nextValidDate?.toString()
            } else null

            planRepository.postPlan(
                childId = childId,
                name = name,
                isRecurring = s.isRecurring,
                startTime = s.formatTimeForServer(s.startTime),
                endTime = s.formatTimeForServer(s.endTime),
                scheduleColor = s.selectedColorType.name,
                dayOfWeek = s.formattedDays.takeIf { s.isRecurring },
                dates = s.selectedDate.takeUnless { s.isRecurring },
                firstOrderDate = firstOrderDateStr
            ).onSuccess {
                val msg = if (isCaseB) {
                    "일정 등록이 마감된 날이 있어, 오늘 이후부터 적용돼요"
                } else {
                    "일정이 등록되었어요."
                }
                _sideEffect.emit(ShowSnackBar(msg))
                delay(200)
                _sideEffect.emit(navigateUp)
            }.onFailure {
                _sideEffect.emit(ShowSnackBar("일정 저장에 실패했어요. 잠시 후 다시 시도해주세요."))
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun onUpdatePlanClick(isIncludeFollowing: Boolean?) {
        if (_state.value.isLoading) return
        val scheduleId = editArgs?.scheduleId ?: return
        val selectedDate = editArgs?.selectedDate ?: return

        viewModelScope.launch {
            val name = textState.text.toString().trim()
            val s = _state.value
            val today = LocalDate.now()
            val now = LocalTime.now()

            val errors = validatePlanInputs(name, s, today, now)
            if (errors.isNotEmpty()) {
                val errorMsg = if (errors.size >= 2) "일정 저장에 실패했어요." else errors.first()
                _sideEffect.emit(ShowSnackBar(errorMsg))
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            val originalIsRecurring = editArgs.isRecurring
            val currentIsRecurring = s.isRecurring
            val finalDayOfWeek: String?
            val finalDates: String?
            val finalIsIncludeFollowing: Boolean?

            if (originalIsRecurring && currentIsRecurring) {
                val originalDayIndices = editArgs.dayOfWeek.toDayIndices()
                val currentDayIndices = s.selectedDays
                if (originalDayIndices != currentDayIndices) {
                    finalDayOfWeek = s.formattedDays
                    finalDates = null
                    finalIsIncludeFollowing = null
                } else {
                    finalDayOfWeek = s.formattedDays
                    finalDates = null
                    finalIsIncludeFollowing = isIncludeFollowing
                }
            } else if (originalIsRecurring && !currentIsRecurring) {
                finalDayOfWeek = null
                finalDates = s.selectedDays.toDateString(s.currentReferenceDate)
                finalIsIncludeFollowing = null
            } else if (!originalIsRecurring && currentIsRecurring) {
                finalDayOfWeek = s.formattedDays
                finalDates = null
                finalIsIncludeFollowing = null
            } else {
                finalDayOfWeek = null
                finalDates = s.selectedDays.toDateString(s.currentReferenceDate)
                finalIsIncludeFollowing = null
            }

            planRepository.updateSchedule(
                scheduleId = scheduleId,
                selectedDate = selectedDate,
                name = name,
                isRecurring = currentIsRecurring,
                startTime = s.formatTimeForServer(s.startTime),
                endTime = s.formatTimeForServer(s.endTime),
                scheduleColor = s.selectedColorType.name,
                dayOfWeek = finalDayOfWeek,
                dates = finalDates,
                isIncludeFollowing = finalIsIncludeFollowing,
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
            val newDays = if (dayIndex in s.selectedDays) s.selectedDays - dayIndex else s.selectedDays + dayIndex
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
                    isRecurring = false,
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

    fun onNextWeek() = _state.update { it.copy(currentReferenceDate = it.currentReferenceDate.plusWeeks(1)) }

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