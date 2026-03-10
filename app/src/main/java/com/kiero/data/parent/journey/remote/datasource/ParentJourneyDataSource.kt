package com.kiero.data.parent.journey.remote.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.journey.remote.dto.response.ParentJourneyResponseDto

interface ParentJourneyDataSource {
    suspend fun getParentJourney(
        childId: Long
    ): BaseResponse<ParentJourneyResponseDto>
}