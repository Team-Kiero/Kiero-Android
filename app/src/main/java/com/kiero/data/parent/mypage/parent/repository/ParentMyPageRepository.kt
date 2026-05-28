package com.kiero.data.parent.mypage.parent.repository

import com.kiero.data.parent.mypage.parent.model.ParentMyProfileModel
import com.kiero.data.parent.mypage.parent.model.ParentTermsModel

interface ParentMyPageRepository {
    suspend fun getParentMyProfile(): Result<ParentMyProfileModel>
    suspend fun getParentTermsExternalLink(): Result<List<ParentTermsModel>>
    suspend fun postWithdrawStatus(): Result<Unit>
}