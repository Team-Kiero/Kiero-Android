package com.kiero.data.parent.plan.remote.datasourceimpl

import PlanAllResponseDto
import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.plan.remote.api.PlanService
import com.kiero.data.parent.plan.remote.datasource.PlanDataSource
import com.kiero.data.parent.plan.remote.dto.request.PlanAddRequestDto
import com.kiero.data.parent.plan.remote.dto.request.PlanDeleteRequestDto
import com.kiero.data.parent.plan.remote.dto.request.PlanUpdateRequestDto
import com.kiero.data.parent.plan.remote.dto.response.PlanColorResponseDto
import javax.inject.Inject

class PlanDataSourceImpl @Inject constructor(
    private val planService: PlanService,
) : PlanDataSource {
    override suspend fun getPlanColor(childId: Long): BaseResponse<PlanColorResponseDto> =
        planService.getPlanColor(childId)

    override suspend fun postPlan(childId: Long, requestDto: PlanAddRequestDto): BaseResponse<Unit> =
        planService.postPlan(childId, requestDto)

    override suspend fun getPlanAll(
        childId: Long,
        startDate: String,
        endDate: String,
    ): BaseResponse<PlanAllResponseDto> =
        planService.getPlanAll(childId, startDate, endDate)

    override suspend fun updateSchedule(
        scheduleId: Long,
        selectedDate: String?,
        startDate: String?,
        endDate: String?,
        requestDto: PlanUpdateRequestDto,
    ): BaseResponse<Unit> =
        planService.updateSchedule(scheduleId, selectedDate, startDate, endDate, requestDto)

    override suspend fun deleteSchedule(
        scheduleId: Long,
        selectedDate: String?,
        startDate: String?,
        endDate: String?,
        requestDto: PlanDeleteRequestDto,
    ): BaseResponse<Unit> =
        planService.deleteSchedule(scheduleId, selectedDate, startDate, endDate, requestDto)
}