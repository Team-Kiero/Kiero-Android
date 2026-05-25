package com.kiero.core.localstorage.permission

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.kiero.core.common.util.suspendRunCatching
import com.kiero.core.localstorage.constant.DataStoreConstant.KEY_PERMISSION_DENIED_COUNT
import com.kiero.core.permission.model.PermissionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PermissionInfoManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
): PermissionInfoManager {
    private fun keyFor(type: PermissionType) =
        intPreferencesKey("${KEY_PERMISSION_DENIED_COUNT}_${type.name}")

    override suspend fun increaseDeniedCount(type: PermissionType) {
        suspendRunCatching {
            dataStore.edit { preferences ->
                val key = keyFor(type)
                preferences[key] = (preferences[key] ?: 0) + 1
            }
        }
    }

    override fun deniedCount(type: PermissionType): Flow<Int> =
        dataStore.data.map { preferences ->
            preferences[keyFor(type)] ?: 0
        }
}
