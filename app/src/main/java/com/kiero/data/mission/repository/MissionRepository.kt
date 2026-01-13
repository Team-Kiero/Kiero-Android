package com.kiero.data.mission.repository

interface MissionRepository {
    suspend fun getMissions(childId: Long? = null)

    suspend fun patchMission(missionId: Long)
}