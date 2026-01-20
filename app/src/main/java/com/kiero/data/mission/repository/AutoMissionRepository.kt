package com.kiero.data.mission.repository

import com.kiero.data.mission.model.MissionCompleteModel
import com.kiero.data.mission.model.MissionSuggestionModel
import com.kiero.data.mission.model.SuggestedMissionModel

interface AutoMissionRepository {

    suspend fun analyzeNotice(
        noticeText: String
    ): Result<MissionSuggestionModel>

    suspend fun saveBatchMissions(
        childId: Long,
        missions: List<SuggestedMissionModel>
    ): Result<List<MissionCompleteModel>>
}