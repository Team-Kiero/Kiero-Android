package com.kiero.data.fcm.remote.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.fcm.remote.dto.PushSettingResponse

interface FcmDataSource {
    suspend fun patchFcmToken(token: String): BaseResponse<Unit>
    suspend fun getPushNotificationSetting(): BaseResponse<PushSettingResponse>
    suspend fun patchPushNotificationSetting(isEnabled: Boolean): BaseResponse<Unit>
}