package com.kiero.data.parent.remote.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.remote.dto.reqeust.ParentMissionAddRequestDto
import com.kiero.data.parent.remote.dto.response.ParentMissionAddResponseDto

interface ParentMissionAddDataSource {
    suspend fun postParentMission(
        childId: Long,
        requestDto: ParentMissionAddRequestDto
    ): BaseResponse<ParentMissionAddResponseDto>
}