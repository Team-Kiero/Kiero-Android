package com.kiero.data.fcm.source

import com.google.firebase.messaging.FirebaseMessaging
import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.fcm.remote.datasource.FirebaseDataSource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDataSourceImpl @Inject constructor() : FirebaseDataSource {
    override suspend fun getFcmToken(): String? {
        return suspendRunCatching {
            FirebaseMessaging.getInstance().token.await()
        }.getOrNull()
    }
}