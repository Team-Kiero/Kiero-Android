package com.kiero.data.parent.mission.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.parent.mission.remote.datasource.ParentMissionAddDataSource
import com.kiero.data.parent.mission.remote.dto.request.ParentMissionAddRequestDto
import com.kiero.data.parent.mission.model.ParentMissionAddModel
import com.kiero.data.parent.mission.model.UpdateMissionModel
import com.kiero.data.parent.mission.model.UpdateMissionResultModel
import com.kiero.data.parent.mission.model.toDto
import com.kiero.data.parent.mission.model.toModel
import com.kiero.data.parent.mission.repository.ParentMissionAddRepository
import javax.inject.Inject

class ParentMissionAddRepositoryImpl @Inject constructor(
    private val dataSource: ParentMissionAddDataSource,
) : ParentMissionAddRepository {
    override suspend fun postParentMission(
        childId: Long,
        name: String,
        reward: Int,
        dueAt: String,
    ): Result<ParentMissionAddModel> = suspendRunCatching {
        val requestDto = ParentMissionAddRequestDto(
            name = name,
            reward = reward,
            dueAt = dueAt
        )

        val response = dataSource.postParentMission(
            childId = childId,
            requestDto = requestDto
        )

        val responseData = response.data ?: throw Exception(response.message)

        responseData.toModel()
    }

    override suspend fun updateMission(
        missionId: Long,
        request: UpdateMissionModel,
    ): Result<UpdateMissionResultModel> = suspendRunCatching {
        dataSource.patchMission(
            missionId = missionId,
            body      = request.toDto(),
        ).data!!.toModel()
    }

    override suspend fun deleteMission(
        missionId: Long,
    ): Result<Unit> = suspendRunCatching {
        dataSource.deleteMission(missionId)
    }
}