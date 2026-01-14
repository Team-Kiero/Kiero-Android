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

    // 실시간 구독 API 추가
    @Streaming
    @GET("api/v1/feeds/{childId}/subscribe")
    suspend fun subscribeAlarmFeed(
        @Header("Authorization") token: String,
        @Header("Accept") accept: String = "text/event-stream", // 명세의 Accept 헤더
        @Path("childId") childId: Long
    ): okhttp3.ResponseBody // 스트림을 직접 읽기 위해 ResponseBody 반환
}