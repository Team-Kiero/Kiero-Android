package com.kiero.data.auth.model

import com.kiero.data.auth.remote.dto.request.kid.AuthKidRequestDto
import com.kiero.data.auth.remote.dto.response.AuthKidResponseDto

data class AuthKidModel(
    val inviteCode: String,
    val lastName: String,
    val firstName: String
)

data class AuthKidResponseModel(
    val lastName: String,
    val firstName: String,
    val role: String,
    val accessToken: String,
    val refreshToken: String
)

fun AuthKidModel.toDto() = AuthKidRequestDto(
    inviteCode = inviteCode,
    lastName = lastName,
    firstName = firstName
)

fun AuthKidResponseDto.toModel() = AuthKidResponseModel(
    lastName = lastName,
    firstName = firstName,
    role = role,
    accessToken = accessToken,
    refreshToken = refreshToken
)

fun AuthKidRequestDto.toModel() = AuthKidModel(
    lastName = lastName,
    firstName = firstName,
    inviteCode = inviteCode
)