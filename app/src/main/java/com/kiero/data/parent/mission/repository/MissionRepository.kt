package com.kiero.data.parent.mission.repository

import com.kiero.data.parent.mission.model.MissionByDateModel
import com.kiero.data.parent.mission.model.MissionCompleteModel

interface MissionRepository {
    suspend fun getMissions(childId: Long? = null) : Result<MissionByDateModel>

    suspend fun patchMission(missionId: Long) : Result<MissionCompleteModel>
}