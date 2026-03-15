package com.kiero.data.parent.journey.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.parent.journey.model.ParentJourneyImageModel
import com.kiero.data.parent.journey.model.ParentJourneyModel
import com.kiero.data.parent.journey.model.toModel
import com.kiero.data.parent.journey.remote.datasource.ParentJourneyDataSource
import com.kiero.data.parent.journey.repository.ParentJourneyRepository
import javax.inject.Inject

class ParentJourneyRepositoryImpl @Inject constructor(
    private val parentJourneyDataSource: ParentJourneyDataSource
) : ParentJourneyRepository {
    override suspend fun getParentJourney(childId: Long): Result<ParentJourneyModel>  =
        suspendRunCatching{
            parentJourneyDataSource.getParentJourney(childId).data!!.toModel()
        }

    override suspend fun patchScheduleImage(scheduleDetailId: Long): Result<ParentJourneyImageModel> =
        suspendRunCatching {
            parentJourneyDataSource.patchScheduleImage(scheduleDetailId).data!!.toModel()
        }
}
