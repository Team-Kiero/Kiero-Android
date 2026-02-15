package com.kiero.domain.repository.parent.mission

import com.kiero.domain.entity.parent.mission.MissionByDateModel
import com.kiero.domain.entity.parent.mission.MissionCompleteModel

interface MissionRepository {
    suspend fun getMissions(childId: Long? = null) : Result<MissionByDateModel>

    suspend fun patchMission(missionId: Long) : Result<MissionCompleteModel>
}