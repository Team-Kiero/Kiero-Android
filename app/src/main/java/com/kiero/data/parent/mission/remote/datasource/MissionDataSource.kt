package com.kiero.data.parent.mission.remote.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.mission.remote.dto.response.MissionByDateResponseDto
import com.kiero.data.parent.mission.remote.dto.response.MissionCompleteResponseDto


interface MissionDataSource {
    suspend fun getMissions(childId: Long? = null) : BaseResponse<MissionByDateResponseDto>
    suspend fun patchMission(missionId: Long) : BaseResponse<MissionCompleteResponseDto>
}