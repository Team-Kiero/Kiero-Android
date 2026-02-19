package com.kiero.data.parent.signup.remote.datasourceimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.signup.remote.api.ParentSignUpService
import com.kiero.data.parent.signup.remote.datasource.ParentSignUpDataSource
import com.kiero.data.parent.signup.remote.dto.reqeust.ParentSignUpRequestDto
import com.kiero.data.parent.signup.remote.dto.response.LinkageKidResponseDto
import com.kiero.data.parent.signup.remote.dto.response.ParentSignUpResponseDto
import javax.inject.Inject

class ParentSignUpDataSourceImpl @Inject constructor(
    private val parentSignUpService: ParentSignUpService
) : ParentSignUpDataSource {
    override suspend fun postSignUp(
        childLastName: String,
        childFirstName: String
    ): BaseResponse<ParentSignUpResponseDto> =
        parentSignUpService.postParentSignUp(
            request = ParentSignUpRequestDto(
                childLastName = childLastName,
                childFirstName = childFirstName
            )
        )

    override suspend fun getLinkageKid(
        childLastName: String,
        childFirstName: String
    ): BaseResponse<LinkageKidResponseDto> =
        parentSignUpService.getLinkageKid(
            childLastName = childLastName,
            childFirstName = childFirstName
        )
}