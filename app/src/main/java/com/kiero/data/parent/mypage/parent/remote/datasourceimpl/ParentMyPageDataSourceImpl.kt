package com.kiero.data.parent.mypage.parent.remote.datasourceimpl

import com.kiero.core.network.di.AuthNetwork
import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.mypage.parent.remote.api.ParentMyPageService
import com.kiero.data.parent.mypage.parent.remote.datasource.ParentMyPageDataSource
import com.kiero.data.parent.mypage.parent.remote.dto.response.parent.ParentMyProfileResponseDto
import com.kiero.data.parent.mypage.parent.remote.dto.response.parent.ParentTermResponseDto
import retrofit2.Response
import javax.inject.Inject

class ParentMyPageDataSourceImpl @Inject constructor(
    private val parentMyPageService: ParentMyPageService
): ParentMyPageDataSource {
    override suspend fun getParentMyProfile(): BaseResponse<ParentMyProfileResponseDto> =
        parentMyPageService.getParentMyProfile()

    override suspend fun getParentTermsExternalLink(): BaseResponse<List<ParentTermResponseDto>> =
        parentMyPageService.getParentTermsExternalLink()

    override suspend fun postWithdrawStatus(): Response<Unit> =
        parentMyPageService.postWithdrawStatus()
}