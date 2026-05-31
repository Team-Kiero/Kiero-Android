package com.kiero.data.parent.mypage.kid.repository

import com.kiero.data.parent.mypage.kid.model.KidTermsModel

interface KidMyPageRepository {
    suspend fun getKidTermsExternalLink(): Result<List<KidTermsModel>>
}