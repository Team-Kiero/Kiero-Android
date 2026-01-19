package com.kiero.data.mission.repository

import com.kiero.presentation.parent.schedule.mission.auto.model.MissionUiModel

interface AutoMissionRepository {
    suspend fun analyzeNotice(noticeText: String): Result<List<MissionUiModel>>

    suspend fun saveBatchMissions(
        childId: Long,
        missions: List<MissionUiModel>
    ): Result<Unit>
}