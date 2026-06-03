package com.kiero.data.kid.user.remote.datasourceimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.kid.user.remote.dto.response.KidParentWithdrawalStatusResponseDto
import com.kiero.data.kid.user.remote.api.KidUserService
import com.kiero.data.kid.user.remote.datasource.KidUserDataSource
import javax.inject.Inject

class KidUserDataSourceImpl @Inject constructor(
    private val kidUserService: KidUserService
) : KidUserDataSource {
    override suspend fun getParentWithdrawalStatus(): BaseResponse<KidParentWithdrawalStatusResponseDto> =
        kidUserService.getParentWithdrawalStatus()
}