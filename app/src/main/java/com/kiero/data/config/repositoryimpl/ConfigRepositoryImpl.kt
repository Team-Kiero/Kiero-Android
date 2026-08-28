package com.kiero.data.config.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.config.model.AppVersionInfo
import com.kiero.data.config.remote.datasource.ConfigRemoteDataSource
import com.kiero.data.config.repository.ConfigRepository
import javax.inject.Inject

class ConfigRepositoryImpl @Inject constructor(
    private val configRemoteDataSource: ConfigRemoteDataSource,
) : ConfigRepository {

    override suspend fun getAppVersionInfo(): Result<AppVersionInfo> =
        suspendRunCatching {
            configRemoteDataSource.getAppVersionInfo()
        }
}
