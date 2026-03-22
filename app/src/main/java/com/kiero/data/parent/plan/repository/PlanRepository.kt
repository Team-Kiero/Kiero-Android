package com.kiero.data.parent.plan.repository

import com.kiero.data.parent.plan.model.PlanAllModel
import com.kiero.data.parent.plan.model.PlanColorModel

interface PlanRepository {
    suspend fun getPlanColor(childId: Long): Result<PlanColorModel>

    suspend fun postPlan(
        childId: Long,
        name: String,
        isRecurring: Boolean,
        startTime: String,
        endTime: String,
        scheduleColor: String,
        dayOfWeek: String?,
        dates: String?,
        firstOrderDate: String?,
    ): Result<Unit>

    suspend fun getPlanAll(
        childId: Long,
        startDate: String,
        endDate: String,
    ): Result<PlanAllModel>

    suspend fun updateSchedule(
        scheduleId: Long,
        selectedDate: String,
        name: String,
        isRecurring: Boolean,
        startTime: String,
        endTime: String,
        scheduleColor: String,
        dayOfWeek: String?,
        dates: String?,
        isIncludeFollowing: Boolean?,
    ): Result<Unit>

    suspend fun deleteSchedule(
        scheduleId: Long,
        selectedDate: String,
        isIncludeFollowing: Boolean?,
    ): Result<Unit>
}