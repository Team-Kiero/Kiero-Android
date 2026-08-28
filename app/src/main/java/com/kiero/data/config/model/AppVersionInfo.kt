package com.kiero.data.config.model

data class AppVersionInfo(
    private val minForceVersion: AppVersion,
    private val latestVersion: AppVersion,
) {
    fun checkUpdateState(currentVersion: AppVersion): UpdateState = when {
        currentVersion < minForceVersion -> UpdateState.FORCE
        currentVersion < latestVersion -> UpdateState.FLEXIBLE
        else -> UpdateState.NONE
    }
}
