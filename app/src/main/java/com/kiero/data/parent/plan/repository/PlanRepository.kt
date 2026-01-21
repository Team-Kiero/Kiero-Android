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
        date: String?,
    ): Result<Unit>

    suspend fun getPlanAll(
        childId: Long,
        startDate: String,
        endDate: String,
    ): Result<PlanAllModel>
}