package com.kiero.data.parent.mypage.kid.remote.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.mypage.kid.remote.dto.response.KidTermResponseDto

interface KidMyPageDataSource {
    suspend fun getKidTermsExternalLink(): BaseResponse<List<KidTermResponseDto>>
}