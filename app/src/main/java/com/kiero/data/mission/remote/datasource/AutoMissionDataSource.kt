package com.kiero.data.mission.remote.datasource

import com.kiero.data.mission.model.MissionCompleteModel
import com.kiero.data.mission.model.MissionSuggestionModel
import com.kiero.data.mission.remote.dto.request.MissionCreateDto

interface AutoMissionDataSource {
    suspend fun analyzeNotice(
        noticeText: String
    ): MissionSuggestionModel

    suspend fun saveBatchMissions(
        childId: Long,
        missions: List<MissionCreateDto>
    ): List<MissionCompleteModel>
}