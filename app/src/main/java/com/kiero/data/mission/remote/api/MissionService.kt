package com.kiero.data.mission.remote.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.mission.remote.dto.response.MissionByDateResponseDto
import com.kiero.data.mission.remote.dto.response.MissionCompleteResponseDto
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface MissionService {
    @GET("api/v1/missions")
    suspend fun getMissions(
        @Query("childId") childId: Long? = null,
    ): BaseResponse<MissionByDateResponseDto>

    @PATCH("api/v1/missions/{missionId}/complete")
    suspend fun completeMission(
        @Query("missionId") missionId: Long
    ): BaseResponse<MissionCompleteResponseDto>
}