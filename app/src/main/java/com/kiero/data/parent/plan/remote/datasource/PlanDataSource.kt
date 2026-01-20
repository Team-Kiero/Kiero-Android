package com.kiero.data.parent.plan.remote.datasource

import PlanAllResponseDto
import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.plan.remote.dto.request.PlanAddRequestDto
import com.kiero.data.parent.plan.remote.dto.response.PlanColorResponseDto

interface PlanDataSource {
    suspend fun getPlanColor(childId: Long): BaseResponse<PlanColorResponseDto>
    suspend fun postPlan(childId: Long, requestDto: PlanAddRequestDto): BaseResponse<Unit>

    suspend fun getPlanAll(
        childId: Long,
        startDate: String,
        endDate: String,
    ): BaseResponse<PlanAllResponseDto>
}