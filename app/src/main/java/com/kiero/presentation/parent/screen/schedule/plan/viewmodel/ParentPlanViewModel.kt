package com.kiero.presentation.parent.screen.schedule.plan.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.data.parent.plan.model.PlanAllModel
import com.kiero.data.parent.plan.model.RecurringScheduleModel
import com.kiero.data.parent.plan.model.ScheduleModel
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

private data class RecurringStartInfo(
    val firstOrderDate: String,
    val hasClosedDateInThisWeek: Boolean,
)

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
        val currentState = _state.value

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
                startTime = currentState.serverTimeToDisplayTime(args.startTime),
                endTime = currentState.serverTimeToDisplayTime(args.endTime),
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

        _state.update { current ->
            val newDays = if (dayIndex in current.selectedDays) {
                current.selectedDays - dayIndex
            } else {
                current.selectedDays + dayIndex
            }
            current.withUpdatedDays(newDays)
        }
    }

    fun onAllDaysSelect(isCurrentlyAllSelected: Boolean) {
        if (isEditMode) return

        _state.update { current ->
            val newDays = if (isCurrentlyAllSelected) emptySet() else (0..6).toSet()
            current.withUpdatedDays(newDays)
        }
    }

    fun onRecurringToggle() {
        if (isEditMode) return

        _state.update { current ->
            if (current.isRecurring) {
                current.copy(
                    isRecurring = false,
                    selectedDate = current.selectedDays.toDateString(current.currentReferenceDate)
                )
            } else {
                current.copy(
                    isRecurring = true,
                    selectedDate = ""
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
                _sideEffect.emit(ShowSnackBar(result.message.orEmpty()))
            }
        }
    }

    fun onPreviousWeek() {
        if (isEditMode) return

        _state.update { current ->
            val thisMonday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            if (current.currentReferenceDate.isAfter(thisMonday)) {
                current.copy(currentReferenceDate = current.currentReferenceDate.minusWeeks(1))
            } else {
                current
            }
        }
    }

    fun onNextWeek() {
        if (isEditMode) return
        _state.update { it.copy(currentReferenceDate = it.currentReferenceDate.plusWeeks(1)) }
    }

    fun onCreatePlanClick() {
        if (isEditMode) onUpdatePlanClick() else onAddPlanClick()
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
            val current = _state.value

            val errors = validateEditInputs(name, current)
            if (errors.isNotEmpty()) {
                val errorMessage = if (errors.size >= 2) "일정 저장에 실패했어요." else errors.first()
                _sideEffect.emit(ShowSnackBar(errorMessage))
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            val scheduleId = editArgs?.scheduleId
            if (scheduleId == null) {
                _state.update { it.copy(isLoading = false) }
                _sideEffect.emit(ShowSnackBar("일정 수정에 실패했습니다"))
                return@launch
            }

            val originalIsRecurring = editArgs.isRecurring
            val weekRange = if (originalIsRecurring) current.currentReferenceDate.toWeekRange() else null

            planRepository.updateSchedule(
                scheduleId = scheduleId,
                selectedDate = if (originalIsRecurring) null else editArgs.selectedDate,
                startDate = weekRange?.first,
                endDate = weekRange?.second,
                name = name,
                isRecurring = current.isRecurring,
                startTime = current.formatTimeForServer(current.startTime),
                endTime = current.formatTimeForServer(current.endTime),
                scheduleColor = current.selectedColorType.name,
                dayOfWeek = current.formattedDays.takeIf { current.isRecurring },
                dates = editArgs.dates.takeUnless { current.isRecurring },
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

    private fun onAddPlanClick() {
        if (_state.value.isLoading) return

        viewModelScope.launch {
            val name = textState.text.toString().trim()
            val current = _state.value
            val today = LocalDate.now()
            val now = LocalTime.now()

            val childId = getChildIdOrReturn() ?: return@launch

            val weekPlanForValidation = if (!current.isRecurring) {
                fetchWeekPlanForValidation(
                    childId = childId,
                    referenceDate = current.currentReferenceDate
                ).getOrElse {
                    _sideEffect.emit(ShowSnackBar("일정 저장에 실패했어요. 잠시 후 다시 시도해주세요."))
                    return@launch
                }
            } else {
                null
            }

            val validationErrors = validatePlanInputs(
                name = name,
                current = current,
                today = today,
                now = now,
                weekPlan = weekPlanForValidation
            )

            if (validationErrors.isNotEmpty()) {
                _sideEffect.emit(ShowSnackBar(validationErrors.first()))
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            val serverStartTime = current.formatTimeForServer(current.startTime)
            val serverEndTime = current.formatTimeForServer(current.endTime)
            val startTimeLocal = parseServerTime(serverStartTime)

            val recurringStartInfo = if (current.isRecurring && startTimeLocal != null) {
                buildRecurringStartInfo(
                    current = current,
                    today = today,
                    now = now,
                    startTime = startTimeLocal,
                )
            } else {
                null
            }

            planRepository.postPlan(
                childId = childId,
                name = name,
                isRecurring = current.isRecurring,
                startTime = serverStartTime,
                endTime = serverEndTime,
                scheduleColor = current.selectedColorType.name,
                dayOfWeek = current.formattedDays.takeIf { current.isRecurring },
                dates = current.selectedDate.takeUnless { current.isRecurring },
                firstOrderDate = recurringStartInfo?.firstOrderDate
            ).onSuccess {
                val message = if (current.isRecurring && recurringStartInfo?.hasClosedDateInThisWeek == true) {
                    "일정 등록이 마감된 날이 있어, 오늘 이후부터 적용돼요"
                } else {
                    "일정이 등록되었어요."
                }
                _sideEffect.emit(ShowSnackBar(message))
                delay(200)
                _sideEffect.emit(navigateUp)
            }.onFailure { throwable ->
                val serverMessage = throwable.message.orEmpty()

                when {
                    serverMessage.contains("기존의 일정과 시간이 중복되는 일정은 추가할 수 없습니다.") -> {
                        _sideEffect.emit(ShowSnackBar("기존의 일정과 시간이 중복되는 일정은 추가할 수 없습니다."))
                    }
                    else -> {
                        _sideEffect.emit(ShowSnackBar("일정 저장에 실패했어요. 잠시 후 다시 시도해주세요."))
                    }
                }

                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun isChanged(): Boolean {
        val args = editArgs ?: return true
        val current = _state.value

        val currentName = textState.text.toString().trim()
        val originalName = args.name.trim()

        val originalColor = runCatching { ColorType.valueOf(args.scheduleColor) }
            .getOrElse { ColorType.fromHexCode(args.scheduleColor) }

        val originalStartTime = current.serverTimeToDisplayTime(args.startTime)
        val originalEndTime = current.serverTimeToDisplayTime(args.endTime)

        return originalName != currentName ||
                originalColor != current.selectedColorType ||
                originalStartTime != current.startTime ||
                originalEndTime != current.endTime
    }

    private fun validateEditInputs(name: String, current: ParentPlanState): List<String> {
        val errors = mutableListOf<String>()

        if (name.isBlank()) {
            errors.add("일정 이름을 입력해주세요.")
        }

        if (current.startTime.isNullOrBlank() || current.endTime.isNullOrBlank()) {
            errors.add("시간을 선택해주세요.")
        } else if (!current.isTimeValid) {
            errors.add("종료시간은 시작시간보다 늦어야 합니다.")
        }

        return errors
    }

    private fun validatePlanInputs(
        name: String,
        current: ParentPlanState,
        today: LocalDate,
        now: LocalTime,
        weekPlan: PlanAllModel? = null,
    ): List<String> {
        val errors = mutableListOf<String>()

        if (name.isBlank()) {
            errors.add("일정 이름을 입력해주세요.")
        }

        if (current.selectedDays.isEmpty()) {
            errors.add("요일을 선택해주세요.")
        }

        if (current.startTime.isNullOrBlank() || current.endTime.isNullOrBlank()) {
            errors.add("시간을 선택해주세요.")
        } else if (!current.isTimeValid) {
            errors.add("종료시간은 시작시간보다 늦어야 합니다.")
        }

        if (errors.isNotEmpty()) return errors

        if (!isEditMode && !current.isRecurring) {
            val startTime = parseServerTime(current.formatTimeForServer(current.startTime))
            val endTime = parseServerTime(current.formatTimeForServer(current.endTime))
            val monday = current.currentReferenceDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            val selectedDates = current.selectedDays.map { monday.plusDays(it.toLong()) }

            if (selectedDates.any { date ->
                    date.isBefore(today) || (date.isEqual(today) && startTime != null && !startTime.isAfter(now))
                }
            ) {
                errors.add("이미 지난 시간에는 일정을 등록할 수 없어요.")
                return errors
            }

            if (weekPlan != null && endTime != null && hasCompletedScheduleAfterEndTime(
                    weekPlan = weekPlan,
                    selectedDates = selectedDates,
                    endTime = endTime
                )
            ) {
                errors.add("이후의 일정이 이미 시작되어, 일정을 추가할 수 없어요.")
                return errors
            }

            if (selectedDates.any { it.isEqual(today) } && current.isFireLit) {
                errors.add("오늘 일정이 마감되어, 일정을 추가할 수 없어요.")
                return errors
            }
        }

        return errors
    }

    private suspend fun fetchWeekPlanForValidation(
        childId: Long,
        referenceDate: LocalDate,
    ): Result<PlanAllModel> {
        val monday = referenceDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toString()
        val sunday = referenceDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).toString()
        return planRepository.getPlanAll(childId, monday, sunday)
    }

    private fun hasCompletedScheduleAfterEndTime(
        weekPlan: PlanAllModel,
        selectedDates: List<LocalDate>,
        endTime: LocalTime,
    ): Boolean {
        return selectedDates.any { targetDate ->
            hasCompletedNormalScheduleAfterEndTime(weekPlan, targetDate, endTime) ||
                    hasCompletedRecurringScheduleAfterEndTime(weekPlan, targetDate, endTime)
        }
    }

    private fun hasCompletedNormalScheduleAfterEndTime(
        weekPlan: PlanAllModel,
        targetDate: LocalDate,
        endTime: LocalTime,
    ): Boolean {
        return weekPlan.normalSchedules.any { schedule ->
            schedule.date.take(10) == targetDate.toString() &&
                    schedule.isCompletedSchedule() &&
                    (schedule.startTime.toLocalTimeOrNull()?.let { !it.isBefore(endTime) } == true)
        }
    }

    private fun hasCompletedRecurringScheduleAfterEndTime(
        weekPlan: PlanAllModel,
        targetDate: LocalDate,
        endTime: LocalTime,
    ): Boolean {
        return weekPlan.recurringSchedules.any { schedule ->
            recurringOccursOnDate(schedule, targetDate) &&
                    schedule.isCompletedSchedule() &&
                    (schedule.startTime.toLocalTimeOrNull()?.let { !it.isBefore(endTime) } == true)
        }
    }

    private fun recurringOccursOnDate(
        schedule: RecurringScheduleModel,
        targetDate: LocalDate,
    ): Boolean {
        val repeatStartDate = runCatching {
            LocalDate.parse(schedule.repeatStartDate.take(10))
        }.getOrNull() ?: return false

        if (targetDate.isBefore(repeatStartDate)) return false

        val targetDayCode = targetDate.dayOfWeek.name.take(3)
        return schedule.dayOfWeek
            .split(",")
            .map { it.trim().uppercase() }
            .contains(targetDayCode)
    }

    private fun ScheduleModel.isCompletedSchedule(): Boolean {
        val status = scheduleStatus?.uppercase()
        return status == "VERIFIED" || status == "COMPLETED"
    }

    private fun String.toLocalTimeOrNull(): LocalTime? {
        return runCatching {
            val parts = split(":")
            LocalTime.of(parts[0].toInt(), parts[1].toInt())
        }.getOrNull()
    }

    private fun buildRecurringStartInfo(
        current: ParentPlanState,
        today: LocalDate,
        now: LocalTime,
        startTime: LocalTime,
    ): RecurringStartInfo {
        val referenceMonday = current.currentReferenceDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        val selectedDatesInReferenceWeek = current.selectedDays
            .sorted()
            .map { referenceMonday.plusDays(it.toLong()) }

        val firstOpenDate = selectedDatesInReferenceWeek.firstOrNull { date ->
            !isClosedForRegistration(
                targetDate = date,
                startTime = startTime,
                today = today,
                now = now,
                isTodayFireLit = current.isFireLit
            )
        }

        val hasClosedDateInThisWeek = selectedDatesInReferenceWeek.any { date ->
            isClosedForRegistration(
                targetDate = date,
                startTime = startTime,
                today = today,
                now = now,
                isTodayFireLit = current.isFireLit
            )
        }

        if (firstOpenDate != null) {
            return RecurringStartInfo(
                firstOrderDate = firstOpenDate.toString(),
                hasClosedDateInThisWeek = hasClosedDateInThisWeek
            )
        }

        val firstSelectedDayOffset = current.selectedDays.minOrNull() ?: 0
        val nextWeekStartDate = referenceMonday
            .plusWeeks(1)
            .plusDays(firstSelectedDayOffset.toLong())

        return RecurringStartInfo(
            firstOrderDate = nextWeekStartDate.toString(),
            hasClosedDateInThisWeek = true
        )
    }

    private fun isClosedForRegistration(
        targetDate: LocalDate,
        startTime: LocalTime,
        today: LocalDate,
        now: LocalTime,
        isTodayFireLit: Boolean,
    ): Boolean {
        return when {
            targetDate.isBefore(today) -> true
            targetDate.isEqual(today) && startTime.isBefore(now) -> true
            targetDate.isEqual(today) && isTodayFireLit -> true
            else -> false
        }
    }

    private fun parseServerTime(serverTime: String): LocalTime? {
        return runCatching {
            val parts = serverTime.split(":")
            LocalTime.of(parts[0].toInt(), parts[1].toInt())
        }.getOrNull()
    }

    private fun LocalDate.toWeekRange(): Pair<String, String> {
        val monday = this.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toString()
        val sunday = this.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).toString()
        return monday to sunday
    }

    private suspend fun getChildIdOrReturn(): Long? {
        val childId = userInfoManager.getChildIdInfo()
        if (childId == null) {
            _state.update { it.copy(isLoading = false) }
        }
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
        if (isRecurring) {
            copy(selectedDays = newDays)
        } else {
            copy(
                selectedDays = newDays,
                selectedDate = newDays.toDateString(currentReferenceDate)
            )
        }

    private fun Set<Int>.toDateString(referenceDate: LocalDate): String {
        val monday = referenceDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        return sorted().joinToString(", ") { monday.plusDays(it.toLong()).toString() }
    }

    private fun String?.toDayIndices(): Set<Int> {
        if (isNullOrBlank()) return emptySet()

        val dayMap = mapOf(
            "MON" to 0,
            "TUE" to 1,
            "WED" to 2,
            "THU" to 3,
            "FRI" to 4,
            "SAT" to 5,
            "SUN" to 6
        )

        return split(",")
            .mapNotNull { dayMap[it.trim()] }
            .toSet()
    }
}