package com.kiero.data.parent.plan.model

import NormalScheduleDto
import PlanAllResponseDto
import RecurringScheduleDto
import com.kiero.data.parent.plan.remote.dto.response.PlanColorResponseDto
import com.kiero.presentation.parent.screen.schedule.model.ScheduleEvent

data class PlanColorModel(
    val scheduleColor : String,
    val colorCode : String,
)


fun PlanColorResponseDto.toModel() = PlanColorModel(
    scheduleColor = this.scheduleColor,
    colorCode = this.colorCode,
)

data class PlanAllModel(
    val isFireLit: Boolean,
    val recurringSchedules: List<RecurringScheduleModel>,
    val normalSchedules: List<NormalScheduleModel>
)
interface ScheduleModel {
    val scheduleId: Long
    val name: String
    val startTime: String
    val endTime: String
    val colorCode: String
}
data class RecurringScheduleModel(
    override val scheduleId: Long,
    override val startTime: String,
    override val endTime: String,
    override val name: String,
    override val colorCode: String,
    val dayOfWeek: String,
    val repeatStartDate: String,
    val repeatEndDate: String? = null,
) : ScheduleModel

data class NormalScheduleModel(
    override val scheduleId: Long,
    override val startTime: String,
    override val endTime: String,
    override val name: String,
    override val colorCode: String,
    val date: String
) : ScheduleModel


fun PlanAllResponseDto.toModel(): PlanAllModel = PlanAllModel(
    isFireLit = this.isFireLit,
    recurringSchedules = this.recurringSchedules.map { it.toModel() },
    normalSchedules = this.normalSchedules.map { it.toModel() }
)

fun RecurringScheduleDto.toModel(): RecurringScheduleModel = RecurringScheduleModel(
    scheduleId = this.scheduleId,
    startTime = this.startTime,
    endTime = this.endTime,
    name = this.name,
    colorCode = this.colorCode,
    dayOfWeek = this.dayOfWeek,
    repeatStartDate = this.repeatStartDate,
    repeatEndDate = this.repeatEndDate,
)

fun NormalScheduleDto.toModel(): NormalScheduleModel = NormalScheduleModel(
    scheduleId = this.scheduleId,
    startTime = this.startTime,
    endTime = this.endTime,
    name = this.name,
    colorCode = this.colorCode,
    date = this.date
)

fun RecurringScheduleModel.toUiModel() = ScheduleEvent(
    id = scheduleId.toString(),
    name = name,
    isRecurring = true,
    startTime = startTime,
    endTime = endTime,
    scheduleColor = colorCode,
    dayOfWeek = dayOfWeek,
    date = null
)

fun NormalScheduleModel.toUiModel() = ScheduleEvent(
    id = scheduleId.toString(),
    name = name,
    isRecurring = false,
    startTime = startTime,
    endTime = endTime,
    scheduleColor = colorCode,
    dayOfWeek = null,
    date = date
)