package com.kiero.data.parent.signup.remote.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.signup.remote.dto.response.ParentSignUpResponseDto

interface ParentSignUpDataSource {
    suspend fun postSignUp(
        childLastName: String,
        childFirstName: String
    ): BaseResponse<ParentSignUpResponseDto>
}