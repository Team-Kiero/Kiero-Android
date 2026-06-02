package com.kiero.data.fcm.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FcmTokenRequest(
    @SerialName("fcmToken")
    val fcmToken: String
)
@Serializable
data class PushSettingRequest(
    val pushNotificationEnabled: Boolean
)

@Serializable
data class PushSettingResponse(
    val pushNotificationEnabled: Boolean
)