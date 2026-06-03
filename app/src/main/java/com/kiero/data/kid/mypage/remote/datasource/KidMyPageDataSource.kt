package com.kiero.data.kid.mypage.remote.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.kid.mypage.remote.dto.response.KidTermResponseDto

interface KidMyPageDataSource {
    suspend fun getKidTermsExternalLink(): BaseResponse<List<KidTermResponseDto>>
}