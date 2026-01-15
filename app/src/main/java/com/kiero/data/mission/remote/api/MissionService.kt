package com.kiero.data.mission.remote.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.mission.dto.response.MissionResponseDto
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface MissionService {
    @GET("api/v1/missions")
    suspend fun getMissions(
        @Query("childId") childId: Long? = null,
    ): BaseResponse<MissionResponseDto>

    @PATCH("api/v1/missions/{missionId}/complete")
    suspend fun completeMission(
        @Query("missionId") missionId: Long
    ): BaseResponse<MissionResponseDto>
}