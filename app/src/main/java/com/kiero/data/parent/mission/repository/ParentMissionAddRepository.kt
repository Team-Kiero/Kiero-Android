package com.kiero.data.parent.mission.repository

import com.kiero.data.parent.mission.model.ParentMissionAddModel

interface ParentMissionAddRepository {
    suspend fun postParentMission(
        childId: Long,
        name: String,
        reward: Int,
        dueAt: String
    ): Result<ParentMissionAddModel>
}