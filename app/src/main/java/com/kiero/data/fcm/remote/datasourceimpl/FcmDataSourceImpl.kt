package com.kiero.data.fcm.remote.datasourceimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.fcm.remote.dto.FcmTokenRequest
import com.kiero.data.fcm.remote.dto.PushSettingRequest
import com.kiero.data.fcm.remote.dto.PushSettingResponse
import com.kiero.data.fcm.remote.api.FcmApi
import com.kiero.data.fcm.remote.datasource.FcmDataSource
import javax.inject.Inject

class FcmDataSourceImpl @Inject constructor(
    private val fcmApi: FcmApi
) : FcmDataSource {

    override suspend fun patchFcmToken(token: String): BaseResponse<Unit> =
        fcmApi.patchFcmToken(FcmTokenRequest(token))

    override suspend fun getPushNotificationSetting(): BaseResponse<PushSettingResponse> =
        fcmApi.getPushNotificationSetting()

    override suspend fun patchPushNotificationSetting(isEnabled: Boolean): BaseResponse<Unit> =
        fcmApi.patchPushNotificationSetting(PushSettingRequest(isEnabled))
}