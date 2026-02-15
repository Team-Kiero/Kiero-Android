package com.kiero.data.parent.mission.remote.datasourceimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.mission.remote.api.MissionService
import com.kiero.data.parent.mission.remote.datasource.MissionDataSource
import com.kiero.data.parent.mission.remote.dto.response.MissionByDateResponseDto
import com.kiero.data.parent.mission.remote.dto.response.MissionCompleteResponseDto
import javax.inject.Inject

class MissionDataSourceImpl @Inject constructor(
    private val missionService: MissionService
) : MissionDataSource {
    override suspend fun getMissions(childId: Long?): BaseResponse<MissionByDateResponseDto> =
        missionService.getMissions(childId)


    override suspend fun patchMission(missionId: Long): BaseResponse<MissionCompleteResponseDto> =
        missionService.completeMission(missionId)
}