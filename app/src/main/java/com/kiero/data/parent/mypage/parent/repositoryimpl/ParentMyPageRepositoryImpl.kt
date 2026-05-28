package com.kiero.data.parent.mypage.parent.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.parent.mypage.parent.model.ParentMyProfileModel
import com.kiero.data.parent.mypage.parent.model.ParentTermsModel
import com.kiero.data.parent.mypage.parent.model.toModel
import com.kiero.data.parent.mypage.parent.remote.datasource.ParentMyPageDataSource
import com.kiero.data.parent.mypage.parent.repository.ParentMyPageRepository
import javax.inject.Inject

class ParentMyPageRepositoryImpl @Inject constructor(
    private val parentMyPageDataSource: ParentMyPageDataSource
) : ParentMyPageRepository {
    override suspend fun getParentMyProfile(): Result<ParentMyProfileModel> = suspendRunCatching {
        parentMyPageDataSource.getParentMyProfile().data!!.toModel()
    }

    override suspend fun getParentTermsExternalLink(): Result<List<ParentTermsModel>> = suspendRunCatching {
        parentMyPageDataSource.getParentTermsExternalLink().data!!.map { it.toModel() }
    }

    override suspend fun postWithdrawStatus(): Result<Unit> = suspendRunCatching {
        parentMyPageDataSource.postWithdrawStatus()
    }
}