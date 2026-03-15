package com.kiero.data.parent.journey.remote.datasourceimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.journey.remote.api.ParentJourneyService
import com.kiero.data.parent.journey.remote.datasource.ParentJourneyDataSource
import com.kiero.data.parent.journey.remote.dto.response.ParentJourneyImageResponseDto
import com.kiero.data.parent.journey.remote.dto.response.ParentJourneyResponseDto
import javax.inject.Inject

class ParentJourneyDataSourceImpl @Inject constructor(
    private val service: ParentJourneyService,
) : ParentJourneyDataSource {
    override suspend fun getParentJourney(childId: Long): BaseResponse<ParentJourneyResponseDto> =
        service.getParentJourney(childId = childId)

    override suspend fun patchScheduleImage(scheduleDetailId: Long): BaseResponse<ParentJourneyImageResponseDto> =
        service.patchScheduleImage(scheduleDetailId = scheduleDetailId)
}
