package com.kiero.data.parent.plan.model

import PlanAllResponseDto
import com.kiero.data.parent.plan.remote.dto.response.PlanColorResponseDto
import com.kiero.presentation.parent.screen.schedule.model.ScheduleEvent
import com.kiero.presentation.parent.screen.schedule.plan.navigation.ScheduleEdit
import java.time.LocalDate

data class PlanColorModel(
    val scheduleColor: String,
    val colorCode: String,
)

fun PlanColorResponseDto.toModel() = PlanColorModel(
    scheduleColor = this.scheduleColor,
    colorCode = this.colorCode,
)

data class PlanAllModel(
    val isFireLit: Boolean,
    val recurringSchedules: List<RecurringScheduleModel>,
    val normalSchedules: List<NormalScheduleModel>,
)
interface ScheduleModel {
    val scheduleId: Long
    val name: String
    val startTime: String
    val endTime: String
    val colorCode: String
    val scheduleStatus: String?
}
data class RecurringScheduleModel(
    override val scheduleId: Long,
    override val startTime: String,
    override val endTime: String,
    override val name: String,
    override val colorCode: String,
    override val scheduleStatus: String? = null,
    val dayOfWeek: String,
    val repeatStartDate: String,
) : ScheduleModel

data class NormalScheduleModel(
    override val scheduleId: Long,
    override val startTime: String,
    override val endTime: String,
    override val name: String,
    override val colorCode: String,
    override val scheduleStatus: String? = null,
    val date: String,
) : ScheduleModel


fun PlanAllResponseDto.toModel(): PlanAllModel {
    val recurring = items
        .filter { it.dayOfWeek.isNotEmpty() }
        .map { item ->
            RecurringScheduleModel(
                scheduleId      = item.scheduleId,
                startTime       = item.startTime,
                endTime         = item.endTime,
                name            = item.name,
                colorCode       = item.colorCode,
                scheduleStatus  = item.scheduleStatus,
                dayOfWeek       = item.dayOfWeek
                    .map { it.uppercase() }
                    .sortedBy { day ->
                        listOf("MON","TUE","WED","THU","FRI","SAT","SUN").indexOf(day)
                    }
                    .joinToString(","),
                repeatStartDate = item.date,
            )
        }

    val normal = items
        .filter { it.dayOfWeek.isEmpty() }
        .map { item ->
            NormalScheduleModel(
                scheduleId     = item.scheduleId,
                startTime      = item.startTime,
                endTime        = item.endTime,
                name           = item.name,
                colorCode      = item.colorCode,
                scheduleStatus = item.scheduleStatus,
                date           = item.date,
            )
        }

    return PlanAllModel(
        isFireLit          = isFireLit,
        recurringSchedules = recurring,
        normalSchedules    = normal,
    )
}

fun RecurringScheduleModel.toUiModel() = ScheduleEvent(
    id            = scheduleId.toString(),
    name          = name,
    isRecurring   = true,
    startTime     = startTime,
    endTime       = endTime,
    scheduleColor = colorCode,
    dayOfWeek     = dayOfWeek,
    date          = repeatStartDate,
)

fun NormalScheduleModel.toUiModel() = ScheduleEvent(
    id            = scheduleId.toString(),
    name          = name,
    isRecurring   = false,
    startTime     = startTime,
    endTime       = endTime,
    scheduleColor = colorCode,
    dayOfWeek     = null,
    date          = date,
)

fun ScheduleModel.toScheduleEditArgs(): ScheduleEdit = when (this) {
    is NormalScheduleModel -> ScheduleEdit(
        scheduleId    = scheduleId,
        selectedDate  = date,
        name          = name,
        isRecurring   = false,
        startTime     = startTime,
        endTime       = endTime,
        scheduleColor = colorCode,
        dayOfWeek     = null,
        dates         = date,
    )
    is RecurringScheduleModel -> ScheduleEdit(
        scheduleId    = scheduleId,
        selectedDate  = repeatStartDate,
        name          = name,
        isRecurring   = true,
        startTime     = startTime,
        endTime       = endTime,
        scheduleColor = colorCode,
        dayOfWeek     = dayOfWeek,
        dates         = null,
    )
    else -> ScheduleEdit(
        scheduleId    = 0L,
        selectedDate  = LocalDate.now().toString(),
        name          = "",
        isRecurring   = false,
        startTime     = "09:00:00",
        endTime       = "10:00:00",
        scheduleColor = "SCHEDULE1",
        dayOfWeek     = null,
        dates         = null,
    )
}

fun ScheduleModel?.toSelectedDate(): String = when (this) {
    is NormalScheduleModel    -> date
    is RecurringScheduleModel -> repeatStartDate
    else                      -> LocalDate.now().toString()
}