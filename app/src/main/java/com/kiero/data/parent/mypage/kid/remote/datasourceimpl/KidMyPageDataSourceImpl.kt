package com.kiero.data.parent.mypage.kid.remote.datasourceimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.mypage.kid.remote.api.KidMyPageService
import com.kiero.data.parent.mypage.kid.remote.datasource.KidMyPageDataSource
import com.kiero.data.parent.mypage.kid.remote.dto.response.KidTermResponseDto
import javax.inject.Inject

class KidMyPageDataSourceImpl @Inject constructor(
    private val kidMyPageService: KidMyPageService
): KidMyPageDataSource {
    override suspend fun getKidTermsExternalLink(): BaseResponse<List<KidTermResponseDto>> =
        kidMyPageService.getKidTermsExternalLink()
}