package com.kiero.data.parent.mypage.remote.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.mypage.remote.dto.response.parent.ParentMyProfileResponseDto
import com.kiero.data.parent.mypage.remote.dto.response.parent.ParentTermResponseDto
import retrofit2.Response

interface ParentMyPageDataSource {
    suspend fun getParentMyProfile(): BaseResponse<ParentMyProfileResponseDto>
    suspend fun getParentTermsExternalLink(): BaseResponse<List<ParentTermResponseDto>>
    suspend fun postWithdrawStatus(): Response<Unit>
}