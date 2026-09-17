package com.kiero.data.auth.remote.dto.request.reviewer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthReviewerRequestDto(
    @SerialName("password")
    val password: String
)
