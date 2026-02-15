package com.kiero.domain.repository.parent.mission

import com.kiero.domain.entity.parent.mission.MissionCompleteModel
import com.kiero.domain.entity.parent.mission.MissionSuggestionModel
import com.kiero.domain.entity.parent.mission.SuggestedMissionModel

interface AutoMissionRepository {

    suspend fun analyzeNotice(
        noticeText: String
    ): Result<MissionSuggestionModel>

    suspend fun saveBatchMissions(
        childId: Long,
        missions: List<SuggestedMissionModel>
    ): Result<List<MissionCompleteModel>>
}