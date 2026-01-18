package com.kiero.data.parent.signup.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.parent.signup.model.ParentSignUpModel
import com.kiero.data.parent.signup.model.toModel
import com.kiero.data.parent.signup.remote.datasource.ParentSignUpDataSource
import com.kiero.data.parent.signup.repository.ParentSignUpRepository
import javax.inject.Inject

class ParentSignUpRepositoryImpl @Inject constructor(
    private val parentSignUpDataSource: ParentSignUpDataSource
) : ParentSignUpRepository {
    override suspend fun postSignUp(
        childLastName: String,
        childFirstName: String
    ): Result<ParentSignUpModel> = suspendRunCatching {
        parentSignUpDataSource.postSignUp(
            childLastName = childLastName,
            childFirstName = childFirstName
        ).data!!.toModel()
    }
}