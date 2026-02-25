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

data class RecurringScheduleModel(
    val startTime: String,
    val endTime: String,
    val name: String,
    val colorCode: String,
    val dayOfWeek: String
)

data class NormalScheduleModel(
    val startTime: String,
    val endTime: String,
    val name: String,
    val colorCode: String,
    val date: String
)


fun PlanAllResponseDto.toModel(): PlanAllModel = PlanAllModel(
    isFireLit = this.isFireLit,
    recurringSchedules = this.recurringSchedules.map { it.toModel() },
    normalSchedules = this.normalSchedules.map { it.toModel() }
)

fun RecurringScheduleDto.toModel(): RecurringScheduleModel = RecurringScheduleModel(
    startTime = this.startTime,
    endTime = this.endTime,
    name = this.name,
    colorCode = this.colorCode,
    dayOfWeek = this.dayOfWeek
)

fun NormalScheduleDto.toModel(): NormalScheduleModel = NormalScheduleModel(
    startTime = this.startTime,
    endTime = this.endTime,
    name = this.name,
    colorCode = this.colorCode,
    date = this.date
)

fun RecurringScheduleModel.toUiModel() = ScheduleEvent(
    id = "recurring_${name}_${startTime}",
    name = name,
    isRecurring = true,
    startTime = startTime,
    endTime = endTime,
    scheduleColor = colorCode,
    dayOfWeek = dayOfWeek,
    date = null
)

fun NormalScheduleModel.toUiModel() = ScheduleEvent(
    id = "normal_${name}_${date}",
    name = name,
    isRecurring = false,
    startTime = startTime,
    endTime = endTime,
    scheduleColor = colorCode,
    dayOfWeek = null,
    date = date
)