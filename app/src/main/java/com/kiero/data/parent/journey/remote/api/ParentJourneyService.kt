package com.kiero.data.parent.journey.remote.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.journey.remote.dto.response.ParentJourneyResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ParentJourneyService {
    @GET("api/v1/parents/progress/{childId}")
    suspend fun getParentJourney(
        @Path("childId") childId: Long
    ): BaseResponse<ParentJourneyResponseDto>
}