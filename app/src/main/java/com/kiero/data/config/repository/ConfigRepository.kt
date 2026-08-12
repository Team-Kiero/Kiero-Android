package com.kiero.data.config.repository

import com.kiero.data.config.model.AppVersionInfo

interface ConfigRepository {
    suspend fun getAppVersionInfo(): Result<AppVersionInfo>
}
