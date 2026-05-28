package com.kiero.data.fcm.repository

interface FcmRepository {
    suspend fun updateFcmToken(token: String): Result<Unit>
    suspend fun getPushSetting(): Result<Boolean>
    suspend fun updatePushSetting(isEnabled: Boolean): Result<Unit>
}