package com.kiero.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    @SerialName("code")
    val code: String,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: T? = null,
)
// TODO : 명세서 확인 후 수정 예정
