package com.kiero.data.parent.mypage.remote.dto.response.parent

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParentMyProfileResponseDto(
    @SerialName("id")
    val id: Int,
    @SerialName("image")
    val image: String?,
    @SerialName("name")
    val name: String,
    @SerialName("hasPendingChildSession")
    val hasPendingChildSession: Boolean,
    @SerialName("pushNotificationEnabled")
    val pushNotificationEnabled: Boolean
)
