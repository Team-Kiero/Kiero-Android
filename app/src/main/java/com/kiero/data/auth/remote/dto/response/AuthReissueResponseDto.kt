package com.kiero.data.auth.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthReissueResponseDto(
    val accessToken: String
)