package com.kiero.domain.repository.parent.signup

import com.kiero.domain.entity.parent.signup.LinkageKidModel
import com.kiero.domain.entity.parent.signup.ParentSignUpModel

interface ParentSignUpRepository {
    suspend fun postSignUp(
        childLastName: String,
        childFirstName: String
    ): Result<ParentSignUpModel>

    suspend fun getLinkageKid(
        childLastName: String,
        childFirstName: String
    ): Result<LinkageKidModel>
}