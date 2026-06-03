package com.kiero.data.parent.mypage.remote.datasourceimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.mypage.remote.dto.response.parent.ParentTermResponseDto
import com.kiero.data.parent.mypage.remote.api.ParentMyPageService
import com.kiero.data.parent.mypage.remote.datasource.ParentMyPageDataSource
import com.kiero.data.parent.mypage.remote.dto.response.parent.ParentMyProfileResponseDto
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