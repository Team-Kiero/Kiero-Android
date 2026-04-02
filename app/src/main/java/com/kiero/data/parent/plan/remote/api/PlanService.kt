package com.kiero.data.parent.plan.remote.api

import PlanAllResponseDto
import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.plan.remote.dto.request.PlanAddRequestDto
import com.kiero.data.parent.plan.remote.dto.request.PlanDeleteRequestDto
import com.kiero.data.parent.plan.remote.dto.request.PlanUpdateRequestDto
import com.kiero.data.parent.plan.remote.dto.response.PlanColorResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PlanService {
    @GET("api/v1/schedules/{childId}/default")
    suspend fun getPlanColor(
        @Path("childId") childId: Long,
    ): BaseResponse<PlanColorResponseDto>

    @POST("api/v1/schedules/{childId}")
    suspend fun postPlan(
        @Path("childId") childId: Long,
        @Body planAddRequestDto: PlanAddRequestDto,
    ): BaseResponse<Unit>

    @Headers("Cache-Control: no-cache")
    @GET("api/v1/schedules/{childId}")
    suspend fun getPlanAll(
        @Path("childId") childId: Long,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
    ): BaseResponse<PlanAllResponseDto>

    @Headers("Cache-Control: no-cache")
    @PATCH("api/v1/schedules/{scheduleId}")
    suspend fun updateSchedule(
        @Path("scheduleId") scheduleId: Long,
        @Query("selectedDate") selectedDate: String,
        @Body request: PlanUpdateRequestDto,
    ): BaseResponse<Unit>

    @Headers("Cache-Control: no-cache")
    @HTTP(method = "DELETE", path = "api/v1/schedules/{scheduleId}", hasBody = true)
    suspend fun deleteSchedule(
        @Path("scheduleId") scheduleId: Long,
        @Query("selectedDate") selectedDate: String,
        @Body request: PlanDeleteRequestDto,
    ): BaseResponse<Unit>
}