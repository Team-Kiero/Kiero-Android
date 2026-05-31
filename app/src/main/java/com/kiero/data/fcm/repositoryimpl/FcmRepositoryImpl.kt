package com.kiero.data.fcm.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.fcm.remote.datasource.FcmDataSource
import com.kiero.data.fcm.remote.datasource.FirebaseDataSource
import com.kiero.data.fcm.repository.FcmRepository
import javax.inject.Inject

class FcmRepositoryImpl @Inject constructor(
    private val fcmDataSource: FcmDataSource,
    private val firebaseDataSource: FirebaseDataSource
) : FcmRepository {

    override suspend fun syncFcmToken(): Result<Unit> = suspendRunCatching {
        val token = firebaseDataSource.getFcmToken()
            ?: throw IllegalStateException("FCM 토큰을 가져올 수 없습니다.")

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