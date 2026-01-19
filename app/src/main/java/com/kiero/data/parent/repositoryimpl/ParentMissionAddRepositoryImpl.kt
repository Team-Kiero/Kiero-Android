package com.kiero.data.parent.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.parent.model.ParentMissionAddModel
import com.kiero.data.parent.model.toModel
import com.kiero.data.parent.remote.datasource.ParentMissionAddDataSource
import com.kiero.data.parent.remote.dto.reqeust.ParentMissionAddRequestDto
import com.kiero.data.parent.repository.ParentMissionAddRepository
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

        dataSource.postParentMission(
            childId = childId,
            requestDto = requestDto
        ).data!!.toModel()
    }
}