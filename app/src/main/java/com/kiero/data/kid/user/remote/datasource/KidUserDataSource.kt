package com.kiero.data.kid.user.remote.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.kid.user.remote.dto.response.KidParentWithdrawalStatusResponseDto

interface KidUserDataSource {
    suspend fun getParentWithdrawalStatus(): BaseResponse<KidParentWithdrawalStatusResponseDto>
}