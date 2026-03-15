package com.kiero.data.parent.mission.remote.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.mission.remote.dto.request.ParentMissionAddRequestDto
import com.kiero.data.parent.mission.remote.dto.request.UpdateMissionRequestDto
import com.kiero.data.parent.mission.remote.dto.response.ParentMissionAddResponseDto
import com.kiero.data.parent.mission.remote.dto.response.UpdateMissionResponseDto

interface ParentMissionAddDataSource {
    suspend fun postParentMission(
        childId: Long,
        requestDto: ParentMissionAddRequestDto
    ): BaseResponse<ParentMissionAddResponseDto>

    suspend fun patchMission(
        missionId: Long,
        body: UpdateMissionRequestDto,
    ): BaseResponse<UpdateMissionResponseDto>

    suspend fun deleteMission(
        missionId: Long,
    ): BaseResponse<Unit>
}