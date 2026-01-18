package com.kiero.data.mission.repository

import com.kiero.data.mission.model.MissionByDateModel
import com.kiero.data.mission.model.MissionCompleteModel

interface MissionRepository {
    suspend fun getMissions(childId: Long? = null) : Result<MissionByDateModel>

    suspend fun patchMission(missionId: Long) : Result<MissionCompleteModel>
}