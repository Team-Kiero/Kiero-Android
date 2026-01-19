package com.kiero.data.parent.repository

import com.kiero.data.parent.model.ParentMissionAddModel

interface ParentMissionAddRepository {
    suspend fun postParentMission(
        childId: Long,
        name: String,
        reward: Int,
        dueAt: String
    ): Result<ParentMissionAddModel>
}