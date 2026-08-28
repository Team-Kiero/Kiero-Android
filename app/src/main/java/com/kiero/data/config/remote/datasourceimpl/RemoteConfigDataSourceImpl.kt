package com.kiero.data.config.remote.datasourceimpl

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.kiero.BuildConfig
import com.kiero.data.config.constant.DEBUG_MIN_FETCH_INTERVAL_SECONDS
import com.kiero.data.config.constant.KEY_LATEST_VERSION
import com.kiero.data.config.constant.KEY_MIN_FORCE_VERSION
import com.kiero.data.config.constant.RELEASE_MIN_FETCH_INTERVAL_SECONDS
import com.kiero.data.config.model.AppVersion
import com.kiero.data.config.model.AppVersionInfo
import com.kiero.data.config.remote.datasource.ConfigRemoteDataSource
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class RemoteConfigDataSourceImpl @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig,
) : ConfigRemoteDataSource {
    private val initializationMutex = Mutex()
    private var isInitialized = false

    override suspend fun getAppVersionInfo(): AppVersionInfo {
        initialize()

        runCatching {
            remoteConfig.fetchAndActivate().await()
        }.onFailure { throwable ->
            Timber.w(throwable, "Remote Config fetch failed. Using the active or in-app defaults.")
        }

        val currentVersion = AppVersion(BuildConfig.VERSION_NAME)
        val minForceVersion = getConfiguredVersion(KEY_MIN_FORCE_VERSION, currentVersion)
        val latestVersion = getConfiguredVersion(KEY_LATEST_VERSION, currentVersion)

        return AppVersionInfo(
            minForceVersion = minForceVersion,
            latestVersion = latestVersion,
        )
    }

    private fun getConfiguredVersion(key: String, fallback: AppVersion): AppVersion =
        AppVersion(remoteConfig.getString(key)).takeIf(AppVersion::isValid) ?: fallback

    private suspend fun initialize() {
        initializationMutex.withLock {
            if (isInitialized) return

            runCatching {
                remoteConfig.setConfigSettingsAsync(
                    remoteConfigSettings {
                        minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) {
                            DEBUG_MIN_FETCH_INTERVAL_SECONDS
                        } else {
                            RELEASE_MIN_FETCH_INTERVAL_SECONDS
                        }
                    },
                ).await()
                remoteConfig.setDefaultsAsync(
                    mapOf(
                        // When no value has been fetched, do not require an update.
                        KEY_MIN_FORCE_VERSION to BuildConfig.VERSION_NAME,
                        KEY_LATEST_VERSION to BuildConfig.VERSION_NAME,
                    ),
                ).await()
            }.onSuccess {
                isInitialized = true
            }.onFailure { throwable ->
                Timber.w(throwable, "Remote Config initialization failed.")
            }
        }
    }
}
