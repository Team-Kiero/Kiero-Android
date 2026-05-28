package com.kiero.data.parent.mypage.kid.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.parent.mypage.kid.model.KidTermsModel
import com.kiero.data.parent.mypage.kid.model.toModel
import com.kiero.data.parent.mypage.kid.remote.datasource.KidMyPageDataSource
import com.kiero.data.parent.mypage.kid.repository.KidMyPageRepository
import javax.inject.Inject

class KidMyPageRepositoryImpl @Inject constructor(
    private val kidMyPageDataSource: KidMyPageDataSource
) : KidMyPageRepository {
    override suspend fun getKidTermsExternalLink(): Result<List<KidTermsModel>> = suspendRunCatching {
        kidMyPageDataSource.getKidTermsExternalLink().data!!.map { it.toModel() }
    }
}