package com.kiero.data.schedule.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.schedule.dto.request.ScheduleCompleteRequestDto
import com.kiero.data.schedule.dto.request.ScheduleImageUploadRequestDto
import com.kiero.data.schedule.dto.response.ScheduleFireResponseDto
import com.kiero.data.schedule.dto.response.ScheduleImageUploadResponseDto
import com.kiero.data.schedule.dto.response.ScheduleSkipResponseDto
import com.kiero.data.schedule.dto.response.ScheduleTodayResponseDto
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ScheduleService {
    @PATCH("/api/v1/schedules/today")
    suspend fun patchScheduleToday(): BaseResponse<ScheduleTodayResponseDto>

    @POST("api/v1/schedule/presigned-url")
    suspend fun postPresignedUrl(
        @Body request: ScheduleImageUploadRequestDto
    ): BaseResponse<ScheduleImageUploadResponseDto>

    @PATCH("/api/v1/schedules/{scheduleDetailId}")
    suspend fun patchScheduleComplete(
        @Path("scheduleDetailId") scheduleDetailId: Long,
        @Body request: ScheduleCompleteRequestDto
    ): BaseResponse<Unit>

    @PATCH("/api/v1/schedules/skip/{scheduleDetailId}")
    suspend fun patchScheduleSkip(
        @Path("scheduleDetailId") scheduleDetailId: Long
    ): BaseResponse<ScheduleSkipResponseDto>

    @PATCH("/api/v1/schedules/fire-lit")
    suspend fun patchScheduleFireLit(): BaseResponse<ScheduleFireResponseDto>
}