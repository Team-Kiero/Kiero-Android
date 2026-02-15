package com.kiero.data.parent.mission.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.parent.mission.remote.datasource.AutoMissionDataSource
import com.kiero.data.parent.mission.remote.dto.request.MissionCreateDto
import com.kiero.domain.entity.parent.mission.MissionCompleteModel
import com.kiero.domain.entity.parent.mission.MissionSuggestionModel
import com.kiero.domain.entity.parent.mission.SuggestedMissionModel
import com.kiero.domain.entity.parent.mission.toModel
import com.kiero.domain.repository.parent.mission.AutoMissionRepository
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class AutoMissionRepositoryImpl @Inject constructor(
    private val autoMissionDataSource: AutoMissionDataSource
) : AutoMissionRepository {

    override suspend fun analyzeNotice(
        noticeText: String
    ): Result<MissionSuggestionModel> = suspendRunCatching {
        withTimeout(15000L) {
            autoMissionDataSource.analyzeNotice(noticeText).data!!.toModel()
        }
    }

    override suspend fun saveBatchMissions(
        childId: Long,
        missions: List<SuggestedMissionModel>
    ): Result<List<MissionCompleteModel>> = suspendRunCatching {
        val requestDto = missions.map { mission ->
            MissionCreateDto(
                name = mission.name,
                reward = mission.reward,
                dueAt = mission.dueAt
            )
        }
        autoMissionDataSource.saveBatchMissions(childId, requestDto).data!!.map { it.toModel() }
    }
}