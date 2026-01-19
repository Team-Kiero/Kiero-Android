package com.kiero.data.mission.remote.datasourceimpl

import com.kiero.data.mission.model.MissionCompleteModel
import com.kiero.data.mission.model.MissionSuggestionModel
import com.kiero.data.mission.model.toModel
import com.kiero.data.mission.remote.api.MissionService
import com.kiero.data.mission.remote.datasource.AutoMissionDataSource
import com.kiero.data.mission.remote.dto.request.MissionBulkCreateRequestDto
import com.kiero.data.mission.remote.dto.request.MissionCreateDto
import com.kiero.data.mission.remote.dto.request.MissionSuggestionRequestDto
import javax.inject.Inject

class AutoMissionDataSourceImpl @Inject constructor(
    private val missionService: MissionService
) : AutoMissionDataSource {

    override suspend fun analyzeNotice(
        noticeText: String
    ): Result<MissionSuggestionModel> =
        runCatching {
            missionService.getSuggestions(
                MissionSuggestionRequestDto(noticeText)
            ).data.toModel()
        }

    override suspend fun saveBatchMissions(
        childId: Long,
        missions: List<MissionCreateDto>
    ): Result<List<MissionCompleteModel>> =
        runCatching {
            missionService.createMissionsBulk(
                childId = childId,
                request = MissionBulkCreateRequestDto(missions)
            ).data.map { it.toModel() }
        }
}