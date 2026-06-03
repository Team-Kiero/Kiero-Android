package com.kiero.data.kid.user.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.kid.user.model.KidParentWithdrawalStatusModel
import com.kiero.data.kid.user.model.toModel
import com.kiero.data.kid.user.remote.datasource.KidUserDataSource
import com.kiero.data.kid.user.repository.KidUserRepository
import javax.inject.Inject

class KidUserRepositoryImpl @Inject constructor(
    private val kidUserDataSource: KidUserDataSource
) : KidUserRepository {
    override suspend fun getParentWithdrawalStatus(): Result<KidParentWithdrawalStatusModel> = suspendRunCatching {
        kidUserDataSource.getParentWithdrawalStatus().data!!.toModel()
    }
}