package com.kiero.data.alarm.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.alarm.dto.response.AlarmFeedResponseDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface AlarmService {
    @GET("api/v1/feeds/{childId}")
    suspend fun getAlarmFeed(
        @Header("Authorization") token: String,
        @Path("childId") childId: Long,
        @Query("size") size: Int? = null,
        @Query("cursor") cursor: String? = null
    ): BaseResponse<AlarmFeedResponseDto>
}