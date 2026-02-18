package com.kiero.core.localstorage.info

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.kiero.core.common.util.suspendRunCatching
import com.kiero.core.localstorage.constant.DataStoreConstant.KEY_CHILD_ID
import com.kiero.core.localstorage.constant.DataStoreConstant.KEY_PARENT_NAME
import com.kiero.core.localstorage.constant.DataStoreConstant.KEY_PARENT_PROFILE_IMAGE
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class UserInfoManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserInfoManager {

    override suspend fun saveParentInfo(parentName: String, parentProfileImage: String) {
        suspendRunCatching {
            dataStore.edit { preferences ->
                preferences[KEY_PARENT_NAME] = parentName
                preferences[KEY_PARENT_PROFILE_IMAGE] = parentProfileImage
            }
        }.onFailure {
            Timber.e(it, "Failed to save parent info")
        }
    }

    override suspend fun getParentInfo(): ParentInfo? {
        return suspendRunCatching {
            dataStore.data.map { preferences ->
                val name = preferences[KEY_PARENT_NAME]
                val image = preferences[KEY_PARENT_PROFILE_IMAGE]

                if (name != null && image != null) {
                    ParentInfo(name, image)
                } else {
                    null
                }
            }.first()
        }.onFailure {
            Timber.e(it, "Failed to get parent info")
        }.getOrNull()
    }

    override suspend fun clearParentInfo() {
        suspendRunCatching {
            dataStore.edit { preferences ->
                preferences.remove(KEY_PARENT_NAME)
                preferences.remove(KEY_PARENT_PROFILE_IMAGE)
            }
        }.onFailure {
            Timber.e(it, "Failed to clear parent info")
        }
    }

    override suspend fun saveChildIdInfo(childId: Long) {
        suspendRunCatching {
            dataStore.edit { preferences ->
                preferences[KEY_CHILD_ID] = childId.toString()
            }
        }.onFailure {
            Timber.e(it, "Failed to save child id info")
        }
    }

    override suspend fun getChildIdInfo(): Long? {
        return suspendRunCatching {
            dataStore.data.map { preferences ->
                val childId = preferences[KEY_CHILD_ID]
                childId?.toLong()
            }.first()
        }.onFailure {
            Timber.e(it, "Failed to get child id info")
        }.getOrNull()
    }
}