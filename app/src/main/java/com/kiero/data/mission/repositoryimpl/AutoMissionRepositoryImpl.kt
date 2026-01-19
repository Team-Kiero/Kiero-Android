package com.kiero.data.mission.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.mission.remote.datasource.AutoMissionDataSource
import com.kiero.data.mission.remote.dto.request.MissionCreateDto
import com.kiero.data.mission.repository.AutoMissionRepository
import com.kiero.presentation.parent.schedule.mission.auto.model.MissionUiModel
import kotlinx.coroutines.withTimeout
import java.time.LocalDate
import javax.inject.Inject

class AutoMissionRepositoryImpl @Inject constructor(
    private val autoMissionDataSource: AutoMissionDataSource
) : AutoMissionRepository {

    override suspend fun analyzeNotice(
        noticeText: String
    ): Result<List<MissionUiModel>> = suspendRunCatching {
        withTimeout(15000L) {
            val response = autoMissionDataSource.analyzeNotice(noticeText).getOrThrow()
            response.suggestedMissions.map { dto ->
                MissionUiModel(
                    name = dto.name,
                    reward = dto.reward,
                    dueAt = LocalDate.parse(dto.dueAt)
                )
            }
        }
    }

    override suspend fun saveBatchMissions(
        childId: Long,
        missions: List<MissionUiModel>
    ): Result<Unit> = suspendRunCatching {
        val requestDto = missions.map { mission ->
            MissionCreateDto(
                name = mission.name,
                reward = mission.reward,
                dueAt = mission.dueAt.toString()
            )
        }

        autoMissionDataSource.saveBatchMissions(childId, requestDto).getOrThrow()
    }
}