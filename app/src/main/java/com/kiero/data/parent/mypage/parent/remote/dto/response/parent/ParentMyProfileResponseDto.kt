package com.kiero.data.parent.mypage.parent.remote.dto.response.parent

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParentMyProfileResponseDto(
    @SerialName("image")
    val image: String?,
    @SerialName("name")
    val name: String,
    @SerialName("hasPendingChildSession")
    val hasPendingChildSession: Boolean,
    @SerialName("pushNotificationEnabled")
    val pushNotificationEnabled: Boolean
)