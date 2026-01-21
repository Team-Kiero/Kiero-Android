package com.kiero.data.mission.remote.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.mission.remote.dto.request.MissionBulkCreateRequestDto
import com.kiero.data.mission.remote.dto.request.MissionSuggestionRequestDto
import com.kiero.data.mission.remote.dto.response.MissionBulkCreateResponseDto
import com.kiero.data.mission.remote.dto.response.MissionByDateResponseDto
import com.kiero.data.mission.remote.dto.response.MissionCompleteResponseDto
import com.kiero.data.mission.remote.dto.response.MissionSuggestionResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.Query

interface MissionService {
    @GET("api/v1/missions")
    suspend fun getMissions(
        @Query("childId") childId: Long? = null,
    ): BaseResponse<MissionByDateResponseDto>

    @PATCH("api/v1/missions/{missionId}/complete")
    suspend fun completeMission(
        @Path("missionId") missionId: Long
    ): BaseResponse<MissionCompleteResponseDto>

    @POST("api/v1/missions/suggestions")
    suspend fun getSuggestions(
        @Body request: MissionSuggestionRequestDto
    ): BaseResponse<MissionSuggestionResponseDto>

    @POST("api/v1/missions/{childId}/bulk")
    suspend fun createMissionsBulk(
        @Path("childId") childId: Long,
        @Body request: MissionBulkCreateRequestDto
    ): BaseResponse<MissionBulkCreateResponseDto>
}