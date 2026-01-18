package com.kiero.data.auth.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthLoginResponseDto(
    @SerialName("name")
    val name: String,
    @SerialName("email")
    val email: String,
    @SerialName("image")
    val image: String,
    @SerialName("role")
    val role: String,
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String
)