package com.kiero.data.parent.mission.repository

import com.kiero.data.parent.mission.model.ParentMissionAddModel
import com.kiero.data.parent.mission.model.UpdateMissionModel
import com.kiero.data.parent.mission.model.UpdateMissionResultModel

interface ParentMissionAddRepository {
    suspend fun postParentMission(
        childId: Long,
        name: String,
        reward: Int,
        dueAt: String
    ): Result<ParentMissionAddModel>

    suspend fun updateMission(
        missionId: Long,
        request: UpdateMissionModel,
    ): Result<UpdateMissionResultModel>

    suspend fun deleteMission(
        missionId: Long,
    ): Result<Unit>
}