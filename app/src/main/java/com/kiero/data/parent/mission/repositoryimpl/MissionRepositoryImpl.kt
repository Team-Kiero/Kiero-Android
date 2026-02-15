package com.kiero.data.parent.mission.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.domain.entity.parent.mission.MissionByDateModel
import com.kiero.domain.entity.parent.mission.MissionCompleteModel
import com.kiero.domain.entity.parent.mission.toModel
import com.kiero.data.parent.mission.remote.datasource.MissionDataSource
import com.kiero.domain.repository.parent.mission.MissionRepository
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