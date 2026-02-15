package com.kiero.data.parent.plan.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.domain.entity.parent.plan.PlanAllModel
import com.kiero.domain.entity.parent.plan.PlanColorModel
import com.kiero.domain.entity.parent.plan.toModel
import com.kiero.data.parent.plan.remote.datasource.PlanDataSource
import com.kiero.data.parent.plan.remote.dto.request.PlanAddRequestDto
import com.kiero.domain.repository.parent.plan.PlanRepository
import javax.inject.Inject

class PlanRepositoryImpl @Inject constructor(
    private val dataSource: PlanDataSource,
) : PlanRepository {
    override suspend fun getPlanColor(childId: Long): Result<PlanColorModel> =
        suspendRunCatching {
            dataSource.getPlanColor(childId).data!!.toModel()
        }

    override suspend fun postPlan(
        childId: Long,
        name: String,
        isRecurring: Boolean,
        startTime: String,
        endTime: String,
        scheduleColor: String,
        dayOfWeek: String?,
        dates: String?,
    ): Result<Unit> = suspendRunCatching {
        val requestDto = PlanAddRequestDto(
            name = name,
            isRecurring = isRecurring,
            startTime = startTime,
            endTime = endTime,
            scheduleColor = scheduleColor,
            dayOfWeek = dayOfWeek,
            dates = dates
        )
        dataSource.postPlan(childId, requestDto)
        Unit
    }

    override suspend fun getPlanAll(
        childId: Long,
        startDate: String,
        endDate: String,
    ): Result<PlanAllModel> = suspendRunCatching {
        dataSource.getPlanAll(
            childId = childId,
            startDate = startDate,
            endDate = endDate
        ).data!!.toModel()
    }
}