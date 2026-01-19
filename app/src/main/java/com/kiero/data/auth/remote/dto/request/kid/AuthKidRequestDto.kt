package com.kiero.data.auth.remote.dto.request.kid

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthKidRequestDto(
    @SerialName("inviteCode")
    val inviteCode: String,
    @SerialName("lastName")
    val lastName: String, // 홍
    @SerialName("firstName")
    val firstName: String // 길동
)
