package com.kiero.data.parent.mission.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.parent.mission.remote.datasource.ParentMissionAddDataSource
import com.kiero.data.parent.mission.remote.dto.request.ParentMissionAddRequestDto
import com.kiero.domain.entity.parent.mission.ParentMissionAddModel
import com.kiero.domain.entity.parent.mission.toModel
import com.kiero.domain.repository.parent.mission.ParentMissionAddRepository
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
}