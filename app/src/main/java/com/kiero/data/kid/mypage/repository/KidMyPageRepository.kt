package com.kiero.data.kid.mypage.repository

import com.kiero.data.kid.mypage.model.KidTermsModel

interface KidMyPageRepository {
    suspend fun getKidTermsExternalLink(): Result<List<KidTermsModel>>
}