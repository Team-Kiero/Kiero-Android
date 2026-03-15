package com.kiero.data.parent.mission.remote.datasourceimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.mission.remote.api.ParentMissionService
import com.kiero.data.parent.mission.remote.datasource.ParentMissionAddDataSource
import com.kiero.data.parent.mission.remote.dto.request.ParentMissionAddRequestDto
import com.kiero.data.parent.mission.remote.dto.request.UpdateMissionRequestDto
import com.kiero.data.parent.mission.remote.dto.response.ParentMissionAddResponseDto
import com.kiero.data.parent.mission.remote.dto.response.UpdateMissionResponseDto
import javax.inject.Inject


class ParentMissionAddDataSourceImpl @Inject constructor(
    private val parentMissionService: ParentMissionService,
) : ParentMissionAddDataSource {
    override suspend fun postParentMission(
        childId: Long,
        requestDto: ParentMissionAddRequestDto
    ): BaseResponse<ParentMissionAddResponseDto> {
        return parentMissionService.postParentMission(
            childId = childId,
            requestDto = requestDto
        )
    }
    override suspend fun patchMission(
        missionId: Long,
        body: UpdateMissionRequestDto,
    ): BaseResponse<UpdateMissionResponseDto> =
        parentMissionService.patchMission(missionId, body)

    override suspend fun deleteMission(
        missionId: Long,
    ): BaseResponse<Unit> =
        parentMissionService.deleteMission(missionId)
}