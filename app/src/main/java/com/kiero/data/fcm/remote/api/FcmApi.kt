package com.kiero.data.fcm.remote.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.fcm.remote.dto.FcmTokenRequest
import com.kiero.data.fcm.remote.dto.PushSettingRequest
import com.kiero.data.fcm.remote.dto.PushSettingResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface FcmApi {
    @PATCH("/api/v1/common/push/fcm-token")
    suspend fun patchFcmToken(
        @Body request: FcmTokenRequest
    ): BaseResponse<Unit>

    @GET("/api/v1/common/push/notification-settings")
    suspend fun getPushNotificationSetting(): BaseResponse<PushSettingResponse>

    @PATCH("/api/v1/common/push/notification-settings")
    suspend fun patchPushNotificationSetting(
        @Body request: PushSettingRequest
    ): BaseResponse<Unit>
}