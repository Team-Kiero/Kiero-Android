package com.kiero.data.mission.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.mission.model.MissionByDateModel
import com.kiero.data.mission.model.MissionCompleteModel
import com.kiero.data.mission.model.toModel
import com.kiero.data.mission.remote.datasource.MissionDataSource
import com.kiero.data.mission.repository.MissionRepository
import javax.inject.Inject

class MissionRepositoryImpl @Inject constructor(
    private val missionDataSource: MissionDataSource
) : MissionRepository {
    override suspend fun getMissions(childId: Long?): Result<MissionByDateModel> =
        suspendRunCatching {
            missionDataSource.getMissions(childId).data!!.toModel()
        }

    override suspend fun patchMission(missionId: Long): Result<MissionCompleteModel> =
        suspendRunCatching {
            missionDataSource.patchMission(missionId).data!!.toModel()
        }
}