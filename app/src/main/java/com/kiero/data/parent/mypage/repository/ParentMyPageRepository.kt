package com.kiero.data.parent.mypage.repository

import com.kiero.data.parent.mypage.model.ParentMyProfileModel
import com.kiero.data.parent.mypage.model.ParentTermsModel

interface ParentMyPageRepository {
    suspend fun getParentMyProfile(): Result<ParentMyProfileModel>
    suspend fun getParentTermsExternalLink(): Result<List<ParentTermsModel>>
    suspend fun postWithdrawStatus(): Result<Unit>
}