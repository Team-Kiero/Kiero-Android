package com.kiero.data.parent.signup.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.domain.entity.parent.signup.LinkageKidModel
import com.kiero.domain.entity.parent.signup.ParentSignUpModel
import com.kiero.domain.entity.parent.signup.toModel
import com.kiero.data.parent.signup.remote.datasource.ParentSignUpDataSource
import com.kiero.domain.repository.parent.signup.ParentSignUpRepository
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

    override suspend fun getLinkageKid(
        childLastName: String,
        childFirstName: String
    ): Result<LinkageKidModel> = suspendRunCatching {
        parentSignUpDataSource.getLinkageKid(
            childLastName = childLastName,
            childFirstName = childFirstName
        ).data!!.toModel()
    }
}