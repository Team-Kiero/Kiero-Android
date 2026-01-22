package com.kiero.data.kid.schedule.remote.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.kid.schedule.remote.dto.request.ScheduleCompleteRequestDto
import com.kiero.data.kid.schedule.remote.dto.request.ScheduleImageUploadRequestDto
import com.kiero.data.kid.schedule.remote.dto.response.ScheduleFireResponseDto
import com.kiero.data.kid.schedule.remote.dto.response.ScheduleImageUploadResponseDto
import com.kiero.data.kid.schedule.remote.dto.response.ScheduleSkipResponseDto
import com.kiero.data.kid.schedule.remote.dto.response.ScheduleTodayResponseDto
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Url

interface ScheduleService {
    @PATCH("api/v1/schedules/today")
    suspend fun patchScheduleToday(): BaseResponse<ScheduleTodayResponseDto>

    @POST("api/v1/presigned-url/schedules")
    suspend fun postPresignedUrl(
        @Body request: ScheduleImageUploadRequestDto
    ): BaseResponse<ScheduleImageUploadResponseDto>

    @PATCH("api/v1/schedules/{scheduleDetailId}")
    suspend fun patchScheduleComplete(
        @Path("scheduleDetailId") scheduleDetailId: Long,
        @Body request: ScheduleCompleteRequestDto
    ): BaseResponse<Unit>

    @PATCH("api/v1/schedules/skip/{scheduleDetailId}")
    suspend fun patchScheduleSkip(
        @Path("scheduleDetailId") scheduleDetailId: Long
    ): BaseResponse<ScheduleSkipResponseDto>

    @PATCH("api/v1/schedules/fire-lit")
    suspend fun patchScheduleFireLit(): BaseResponse<ScheduleFireResponseDto>
}