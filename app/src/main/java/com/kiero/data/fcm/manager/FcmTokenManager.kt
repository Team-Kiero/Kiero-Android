package com.kiero.data.fcm.manager

import com.google.firebase.messaging.FirebaseMessaging
import com.kiero.core.common.util.suspendRunCatching // 유저님이 만드신 유틸
import kotlinx.coroutines.tasks.await
import timber.log.Timber

object FcmTokenManager {
    suspend fun getToken(): String? {
        return suspendRunCatching {
            FirebaseMessaging.getInstance().token.await()
        }.onFailure { exception ->
            Timber.e(exception, "FCM 토큰 획득 실패")
        }.getOrNull()
    }
}