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

        textState.edit { replace(0, length, args.name.trim()) }

        val initialColor = runCatching { ColorType.valueOf(args.scheduleColor) }
            .getOrElse { ColorType.fromHexCode(args.scheduleColor) }

        _state.update {
            it.copy(
                isRecurring = args.isRecurring,
                startTime = state.serverTimeToDisplayTime(args.startTime),
                endTime = state.serverTimeToDisplayTime(args.endTime),
                selectedColorType = initialColor,
                selectedDays = selectedDays,
                selectedDate = args.dates.orEmpty(),
            )
        }
    }

    fun showToastMessage(message: String) {
        viewModelScope.launch {
            _sideEffect.emit(ShowSnackBar(message))
        }
    }

    fun onDayClick(dayIndex: Int) {
        if (isEditMode) return
        _state.update { s ->
            val newDays = if (dayIndex in s.selectedDays) s.selectedDays - dayIndex else s.selectedDays + dayIndex
            s.withUpdatedDays(newDays)
        }
    }

    fun onAllDaysSelect(isCurrentlyAllSelected: Boolean) {
        if (isEditMode) return
        _state.update { s ->
            val newDays = if (isCurrentlyAllSelected) emptySet() else (0..6).toSet()
            s.withUpdatedDays(newDays)
        }
    }

    fun onRecurringToggle() {
        if (isEditMode) return
        _state.update { s ->
            if (s.isRecurring) s.copy(isRecurring = false, selectedDate = s.selectedDays.toDateString(s.currentReferenceDate))
            else s.copy(isRecurring = true, selectedDate = "")
        }
    }

    fun onTimeSelected(isStart: Boolean, time: String) {
        if (isEditMode) return
        viewModelScope.launch {
            val result = _state.value.validateAndTimeAdjustment(time)
            _state.update {
                if (isStart) it.copy(startTime = result.adjustedTime)
                else it.copy(endTime = result.adjustedTime)
            }
            if (!result.isValid) _sideEffect.emit(ShowSnackBar(result.message.orEmpty()))
        }
    }

    fun onPreviousWeek() {
        if (isEditMode) return
        _state.update { s ->
            val thisMonday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            if (s.currentReferenceDate.isAfter(thisMonday)) s.copy(currentReferenceDate = s.currentReferenceDate.minusWeeks(1))
            else s
        }
    }

    fun onNextWeek() {
        if (isEditMode) return
        _state.update { it.copy(currentReferenceDate = it.currentReferenceDate.plusWeeks(1)) }
    }

    private fun isChanged(): Boolean {
        val args = editArgs ?: return true
        val s = _state.value

        val currentName = textState.text.toString().trim()
        val originalName = args.name.trim()

        val originalColor = runCatching { ColorType.valueOf(args.scheduleColor) }
            .getOrElse { ColorType.fromHexCode(args.scheduleColor) }

        return originalName != currentName || originalColor != s.selectedColorType
    }

    fun onUpdatePlanClick() {
        if (_state.value.isLoading) return

        if (!isChanged()) {
            viewModelScope.launch {
                _sideEffect.emit(ShowSnackBar("변경사항이 없어요."))
            }
            return
        }

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

            val scheduleId = editArgs?.scheduleId ?: return@launch
            val originalIsRecurring = editArgs.isRecurring

            val selectedDateParam = if (!originalIsRecurring) editArgs.selectedDate else null
            val startDateParam = if (originalIsRecurring) s.currentReferenceDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toString() else null
            val endDateParam = if (originalIsRecurring) s.currentReferenceDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).toString() else null

            planRepository.updateSchedule(
                scheduleId = scheduleId,
                selectedDate = selectedDateParam,
                startDate = startDateParam,
                endDate = endDateParam,
                name = name,
                isRecurring = s.isRecurring,
                startTime = s.formatTimeForServer(s.startTime),
                endTime = s.formatTimeForServer(s.endTime),
                scheduleColor = s.selectedColorType.name,
                dayOfWeek = s.formattedDays.takeIf { s.isRecurring },
                dates = editArgs.dates.takeUnless { s.isRecurring },
                isIncludeFollowing = true
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

    fun onCreatePlanClick() {
        if (isEditMode) onUpdatePlanClick() else onAddPlanClick()
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
                _sideEffect.emit(ShowSnackBar(errors.first()))
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            val childId = getChildIdOrReturn() ?: return@launch

            val serverTime = s.formatTimeForServer(s.startTime)
            val startTimeLocal = runCatching {
                val timeParts = serverTime.split(":")
                LocalTime.of(timeParts[0].toInt(), timeParts[1].toInt())
            }.getOrNull()

            val isPastTimeToday = startTimeLocal != null && startTimeLocal.isBefore(now)

            var isDelayedStart = false
            var firstOrderDateStr: String? = null

            if (s.isRecurring) {
                val referenceMonday = s.currentReferenceDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                val selectedDatesInRefWeek = s.selectedDays.map { referenceMonday.plusDays(it.toLong()) }.sorted()

                var firstValidDate: LocalDate? = null

                for (date in selectedDatesInRefWeek) {
                    if (date.isBefore(today)) {
                        isDelayedStart = true
                        continue
                    }
                    if (date.isEqual(today)) {
                        if (isPastTimeToday || s.isFireLit) {
                            isDelayedStart = true
                            continue
                        }
                    }
                    if (firstValidDate == null) {
                        firstValidDate = date
                    }
                }

                if (firstValidDate == null) {
                    val nextWeekMonday = referenceMonday.plusWeeks(1)
                    val firstSelectedDayOffset = s.selectedDays.minOrNull() ?: 0
                    firstValidDate = nextWeekMonday.plusDays(firstSelectedDayOffset.toLong())
                    isDelayedStart = true
                }

                firstOrderDateStr = firstValidDate.toString()
            }

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
                val msg = if (s.isRecurring && isDelayedStart) {
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

    private fun validatePlanInputs(name: String, s: ParentPlanState, today: LocalDate, now: LocalTime): List<String> {
        val errors = mutableListOf<String>()
        if (name.isBlank()) errors.add("일정 이름을 입력해주세요.")
        if (s.selectedDays.isEmpty()) errors.add("요일을 선택해주세요.")
        if (s.startTime.isNullOrBlank() || s.endTime.isNullOrBlank()) {
            errors.add("시간을 선택해주세요.")
        } else if (!s.isTimeValid) {
            errors.add("종료시간은 시작시간보다 늦어야 합니다.")
        }

        val serverTime = s.formatTimeForServer(s.startTime)
        val startTimeLocal = runCatching {
            val parts = serverTime.split(":")
            LocalTime.of(parts[0].toInt(), parts[1].toInt())
        }.getOrNull()
        val isPastTimeToday = startTimeLocal != null && startTimeLocal.isBefore(now)

        if (!isEditMode && !s.isRecurring) {
            val monday = s.currentReferenceDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            val selectedDates = s.selectedDays.map { monday.plusDays(it.toLong()) }

            for (date in selectedDates) {
                when {
                    date.isBefore(today) -> {
                        errors.add("이미 지난 시간에는 일정을 등록할 수 없어요.")
                        break
                    }
                    date.isEqual(today) -> {
                        if (isPastTimeToday) {
                            errors.add("이미 지난 시간에는 일정을 등록할 수 없어요.")
                            break
                        }
                        if (s.isFireLit) {
                            errors.add("오늘 일정이 마감되어, 일정을 추가할 수 없어요.")
                            break
                        }
                    }
                }
            }
        }
        return errors
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

    fun onColorSelected(colorType: ColorType) {
        _state.update { it.copy(selectedColorType = colorType, showColorPicker = false) }
    }

    fun toggleColorPicker(show: Boolean) {
        _state.update { it.copy(showColorPicker = show) }
    }

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