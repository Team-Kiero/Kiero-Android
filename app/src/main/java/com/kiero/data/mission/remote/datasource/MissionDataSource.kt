package com.kiero.data.mission.remote.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.mission.remote.dto.response.MissionCompleteResponseDto
import com.kiero.data.mission.remote.dto.response.MissionByDateResponseDto

interface MissionDataSource {
    suspend fun getMissions(childId: Long? = null) : BaseResponse<MissionByDateResponseDto>
    suspend fun patchMission(missionId: Long) : BaseResponse<MissionCompleteResponseDto>
}