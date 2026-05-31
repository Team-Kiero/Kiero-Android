package com.kiero.data.fcm.repository

interface FcmRepository {
    suspend fun syncFcmToken(): Result<Unit>
    suspend fun getPushSetting(): Result<Boolean>
    suspend fun updatePushSetting(isEnabled: Boolean): Result<Unit>
}