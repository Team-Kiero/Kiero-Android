package com.kiero.core.localstorage.onboarding

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.kiero.core.common.util.suspendRunCatching
import com.kiero.core.localstorage.constant.DataStoreConstant.KEY_IS_SAW_ONBOARDING
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class OnboardingManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : OnboardingManager {
    override suspend fun saveIsSawOnboarding(isSaw: Boolean) {
        suspendRunCatching {
            dataStore.edit {
                it[KEY_IS_SAW_ONBOARDING] = isSaw
            }
        }
    }

    override suspend fun getIsSawOnboarding(): Boolean {
        return suspendRunCatching {
            val isSaw = dataStore.data.map { it[KEY_IS_SAW_ONBOARDING] }.first()
            isSaw ?: false
        }.onFailure { Timber.e(it) }.getOrDefault(false)
    }
}