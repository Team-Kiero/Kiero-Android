package com.kiero.data.auth.model

import com.kiero.data.auth.remote.dto.response.AuthLoginResponseDto

data class AuthLoginModel(
    val id: Int,
    val name: String,
    val image: String
)

fun AuthLoginResponseDto.toModel() = AuthLoginModel(
    id = id,
    name = name,
    image = image
)
