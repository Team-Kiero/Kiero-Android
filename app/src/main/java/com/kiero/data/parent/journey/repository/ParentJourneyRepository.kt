package com.kiero.data.parent.journey.repository

import com.kiero.data.parent.journey.model.ParentJourneyImageModel
import com.kiero.data.parent.journey.model.ParentJourneyModel

interface ParentJourneyRepository {
    suspend fun getParentJourney(
        childId: Long
    ): Result<ParentJourneyModel>

    suspend fun patchScheduleImage(
        scheduleDetailId: Long
    ): Result<ParentJourneyImageModel>
}
