package com.kiero.data.parent.mission.remote.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.mission.remote.dto.request.MissionCreateDto
import com.kiero.data.parent.mission.remote.dto.response.MissionBulkCreateResponseDto
import com.kiero.data.parent.mission.remote.dto.response.MissionSuggestionResponseDto


interface AutoMissionDataSource {
    suspend fun analyzeNotice(
        noticeText: String
    ): BaseResponse<MissionSuggestionResponseDto>

    suspend fun saveBatchMissions(
        childId: Long,
        missions: List<MissionCreateDto>
    ): BaseResponse<MissionBulkCreateResponseDto>
}