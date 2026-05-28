package com.kiero.data.fcm.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.fcm.remote.datasource.FcmDataSource
import com.kiero.data.fcm.repository.FcmRepository
import javax.inject.Inject

class FcmRepositoryImpl @Inject constructor(
    private val fcmDataSource: FcmDataSource
) : FcmRepository {

    override suspend fun updateFcmToken(token: String): Result<Unit> =
        suspendRunCatching {
            fcmDataSource.patchFcmToken(token)
        }

    override suspend fun getPushSetting(): Result<Boolean> =
        suspendRunCatching {
            fcmDataSource.getPushNotificationSetting().data?.pushNotificationEnabled ?: false
        }

    override suspend fun updatePushSetting(isEnabled: Boolean): Result<Unit> =
        suspendRunCatching {
            fcmDataSource.patchPushNotificationSetting(isEnabled)
        }
}