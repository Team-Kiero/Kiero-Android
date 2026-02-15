package com.kiero.domain.entity.parent.signup

import com.kiero.data.parent.signup.remote.dto.response.ParentSignUpResponseDto

data class ParentSignUpModel(
    val code: String,
    val childLastName: String,
    val childFirstName: String
)

fun ParentSignUpResponseDto.toModel(): ParentSignUpModel = ParentSignUpModel(
    code = code,
    childLastName = childLastName,
    childFirstName = childFirstName
)



