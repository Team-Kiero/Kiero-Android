package com.kiero.data.kid.mypage.remote.datasourceimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.kid.mypage.remote.api.KidMyPageService
import com.kiero.data.kid.mypage.remote.datasource.KidMyPageDataSource
import com.kiero.data.kid.mypage.remote.dto.response.KidTermResponseDto
import javax.inject.Inject

class KidMyPageDataSourceImpl @Inject constructor(
    private val kidMyPageService: KidMyPageService
): KidMyPageDataSource {
    override suspend fun getKidTermsExternalLink(): BaseResponse<List<KidTermResponseDto>> =
        kidMyPageService.getKidTermsExternalLink()
}