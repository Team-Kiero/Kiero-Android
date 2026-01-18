package com.kiero.data.parent.signup.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParentSignUpResponseDto(
    @SerialName("code")
    val code: String,
    @SerialName("childLastName")
    val childLastName: String,
    @SerialName("childFirstName")
    val childFirstName: String
)
