package com.kiero.data.parent.mission.remote.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.mission.remote.dto.request.ParentMissionAddRequestDto
import com.kiero.data.parent.mission.remote.dto.response.ParentMissionAddResponseDto

interface ParentMissionAddDataSource {
    suspend fun postParentMission(
        childId: Long,
        requestDto: ParentMissionAddRequestDto
    ): BaseResponse<ParentMissionAddResponseDto>
}