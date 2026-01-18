package com.kiero.data.alarm.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.alarm.dto.response.AlarmFeedResponseDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

interface AlarmService {
    @GET("api/v1/feeds/{childId}")
    suspend fun getAlarmFeed(
        @Header("Authorization") token: String,
        @Path("childId") childId: Long,
        @Query("size") size: Int? = null,
        @Query("cursor") cursor: String? = null
    ): BaseResponse<AlarmFeedResponseDto>

    @Streaming
    @GET("api/v1/feeds/{childId}/subscribe")
    suspend fun subscribeAlarmFeed(
        @Header("Authorization") token: String,
        @Header("Accept") accept: String = "text/event-stream",
        @Path("childId") childId: Long
    ): okhttp3.ResponseBody
}