package com.kiero.data.parent.mission.repository

import com.kiero.data.parent.mission.model.MissionCompleteModel
import com.kiero.data.parent.mission.model.MissionSuggestionModel
import com.kiero.data.parent.mission.model.SuggestedMissionModel

interface AutoMissionRepository {

    suspend fun analyzeNotice(
        noticeText: String
    ): Result<MissionSuggestionModel>

    suspend fun saveBatchMissions(
        childId: Long,
        missions: List<SuggestedMissionModel>
    ): Result<List<MissionCompleteModel>>
}