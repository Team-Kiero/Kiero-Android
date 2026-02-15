package com.kiero.domain.repository.parent.plan

import com.kiero.domain.entity.parent.plan.PlanAllModel
import com.kiero.domain.entity.parent.plan.PlanColorModel

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
    ): Result<Unit>

    suspend fun getPlanAll(
        childId: Long,
        startDate: String,
        endDate: String,
    ): Result<PlanAllModel>
}