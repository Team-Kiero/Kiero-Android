package com.kiero.data.config.remote.datasource

import com.kiero.data.config.model.AppVersionInfo

interface ConfigRemoteDataSource {
    suspend fun getAppVersionInfo(): AppVersionInfo
}
