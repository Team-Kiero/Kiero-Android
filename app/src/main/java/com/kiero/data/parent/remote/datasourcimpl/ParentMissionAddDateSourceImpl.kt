package com.kiero.data.parent.remote.datasourcimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.remote.api.ParentMissionService
import com.kiero.data.parent.remote.datasource.ParentMissionAddDataSource
import com.kiero.data.parent.remote.dto.reqeust.ParentMissionAddRequestDto
import com.kiero.data.parent.remote.dto.response.ParentMissionAddResponseDto
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
}