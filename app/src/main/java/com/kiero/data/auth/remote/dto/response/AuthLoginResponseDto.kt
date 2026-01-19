package com.kiero.data.auth.remote.dto.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthKidResponseDto(
    @SerialName("lastName")
    val lastName: String,
    @SerialName("firstName")
    val firstName: String,
    @SerialName("role")
    val role: String,
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String
)