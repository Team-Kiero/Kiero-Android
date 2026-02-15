package com.kiero.domain.repository.parent.mission

import com.kiero.domain.entity.parent.mission.ParentMissionAddModel

interface ParentMissionAddRepository {
    suspend fun postParentMission(
        childId: Long,
        name: String,
        reward: Int,
        dueAt: String
    ): Result<ParentMissionAddModel>
}