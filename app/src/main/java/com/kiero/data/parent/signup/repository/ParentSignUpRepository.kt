package com.kiero.data.parent.signup.repository

import com.kiero.data.parent.signup.model.LinkageKidModel
import com.kiero.data.parent.signup.model.ParentSignUpModel

interface ParentSignUpRepository {
    /**
     * 초대 코드 발급
     * */
    suspend fun postSignUp(
        childLastName: String,
        childFirstName: String
    ): Result<ParentSignUpModel>

    suspend fun getLinkageKid(
        childLastName: String,
        childFirstName: String
    ): Result<LinkageKidModel>
}