package com.kiero.data.kid.mypage.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.kid.mypage.model.KidTermsModel
import com.kiero.data.kid.mypage.model.toModel
import com.kiero.data.kid.mypage.remote.datasource.KidMyPageDataSource
import com.kiero.data.kid.mypage.repository.KidMyPageRepository
import javax.inject.Inject

class KidMyPageRepositoryImpl @Inject constructor(
    private val kidMyPageDataSource: KidMyPageDataSource
) : KidMyPageRepository {
    override suspend fun getKidTermsExternalLink(): Result<List<KidTermsModel>> = suspendRunCatching {
        kidMyPageDataSource.getKidTermsExternalLink().data!!.map { it.toModel() }
    }
}