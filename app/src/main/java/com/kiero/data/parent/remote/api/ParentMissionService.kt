package com.kiero.data.parent.remote.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.remote.dto.reqeust.ParentMissionAddRequestDto
import com.kiero.data.parent.remote.dto.response.ParentMissionAddResponseDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ParentMissionService {
    @POST("api/v1/missions/{childId}")
    suspend fun postParentMission(
        @Path("childId") childId: Long,
        @Body requestDto: ParentMissionAddRequestDto,
    ): BaseResponse<ParentMissionAddResponseDto>
}