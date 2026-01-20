package com.kiero.data.mission.remote.datasourceimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.mission.remote.api.MissionService
import com.kiero.data.mission.remote.datasource.AutoMissionDataSource
import com.kiero.data.mission.remote.dto.request.MissionBulkCreateRequestDto
import com.kiero.data.mission.remote.dto.request.MissionCreateDto
import com.kiero.data.mission.remote.dto.request.MissionSuggestionRequestDto
import com.kiero.data.mission.remote.dto.response.MissionBulkCreateResponseDto
import com.kiero.data.mission.remote.dto.response.MissionSuggestionResponseDto
import javax.inject.Inject

class AutoMissionDataSourceImpl @Inject constructor(
    private val missionService: MissionService
) : AutoMissionDataSource {

    override suspend fun analyzeNotice(
        noticeText: String
    ): BaseResponse<MissionSuggestionResponseDto> =
        missionService.getSuggestions(
            MissionSuggestionRequestDto(noticeText)
        )

    override suspend fun saveBatchMissions(
        childId: Long,
        missions: List<MissionCreateDto>
    ): BaseResponse<MissionBulkCreateResponseDto> =
        missionService.createMissionsBulk(
            childId,
            MissionBulkCreateRequestDto(missions)
        )
}