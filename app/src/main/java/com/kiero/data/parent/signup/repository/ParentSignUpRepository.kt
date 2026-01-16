package com.kiero.data.parent.signup.repository

import com.kiero.data.parent.signup.model.ParentSignUpModel

interface ParentSignUpRepository {
    suspend fun postSignUp(
        childLastName: String,
        childFirstName: String
    ): Result<ParentSignUpModel>
}