package com.kiero.data.parent.mission.remote.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.mission.remote.dto.request.ParentMissionAddRequestDto
import com.kiero.data.parent.mission.remote.dto.request.UpdateMissionRequestDto
import com.kiero.data.parent.mission.remote.dto.response.ParentMissionAddResponseDto
import com.kiero.data.parent.mission.remote.dto.response.UpdateMissionResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ParentMissionService {
    @POST("api/v1/missions/{childId}")
    suspend fun postParentMission(
        @Path("childId") childId: Long,
        @Body requestDto: ParentMissionAddRequestDto,
    ): BaseResponse<ParentMissionAddResponseDto>

    @PATCH("api/v1/missions/{missionId}")
    suspend fun patchMission(
        @Path("missionId") missionId: Long,
        @Body body: UpdateMissionRequestDto,
    ): BaseResponse<UpdateMissionResponseDto>

    @DELETE("api/v1/missions/{missionId}")
    suspend fun deleteMission(
        @Path("missionId") missionId: Long,
    ): BaseResponse<Unit>
}