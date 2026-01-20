package com.kiero.data.mission.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.mission.model.MissionCompleteModel
import com.kiero.data.mission.model.MissionSuggestionModel
import com.kiero.data.mission.model.SuggestedMissionModel
import com.kiero.data.mission.remote.datasource.AutoMissionDataSource
import com.kiero.data.mission.remote.dto.request.MissionCreateDto
import com.kiero.data.mission.repository.AutoMissionRepository
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class AutoMissionRepositoryImpl @Inject constructor(
    private val autoMissionDataSource: AutoMissionDataSource
) : AutoMissionRepository {

    override suspend fun analyzeNotice(
        noticeText: String
    ): Result<MissionSuggestionModel> = suspendRunCatching {
        withTimeout(15000L) {
            autoMissionDataSource.analyzeNotice(noticeText).data!!
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
        autoMissionDataSource.saveBatchMissions(childId, requestDto).data!!
    }
}