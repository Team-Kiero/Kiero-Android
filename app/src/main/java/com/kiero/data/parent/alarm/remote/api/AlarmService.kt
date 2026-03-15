package com.kiero.data.parent.alarm.remote.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.alarm.remote.dto.response.AlarmFeedResponseDto
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface AlarmService {
    @PATCH("api/v1/feeds/{childId}")
    suspend fun getAlarmFeed(
        @Path("childId") childId: Long,
        @Query("size") size: Int? = null,
        @Query("cursor") cursor: String? = null
    ): BaseResponse<AlarmFeedResponseDto>
}