package com.kiero.data.parent.signup.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParentSignUpRequestDto(
    @SerialName("childLastName")
    val childLastName: String,
    @SerialName("childFirstName")
    val childFirstName: String
)