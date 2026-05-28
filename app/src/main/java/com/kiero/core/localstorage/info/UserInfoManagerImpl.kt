package com.kiero.core.localstorage.info

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.kiero.core.common.util.suspendRunCatching
import com.kiero.core.localstorage.constant.DataStoreConstant
import com.kiero.core.localstorage.constant.DataStoreConstant.KEY_AGREED_TERMS_IDS
import com.kiero.core.localstorage.constant.DataStoreConstant.KEY_CHILD_FIRST_NAME
import com.kiero.core.localstorage.constant.DataStoreConstant.KEY_CHILD_ID
import com.kiero.core.localstorage.constant.DataStoreConstant.KEY_CHILD_LAST_NAME
import com.kiero.core.localstorage.constant.DataStoreConstant.KEY_IS_REQUIRED_TERMS_ALL_AGREED
import com.kiero.core.localstorage.constant.DataStoreConstant.KEY_PARENT_NAME
import com.kiero.core.localstorage.constant.DataStoreConstant.KEY_PARENT_PROFILE_IMAGE
import com.kiero.core.localstorage.constant.DataStoreConstant.KEY_PENDING_INVITE_CODE
import com.kiero.core.localstorage.constant.DataStoreConstant.KEY_PENDING_INVITE_EXPIRE_TIME
import com.kiero.core.model.parent.ParentInfo
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
                DataStoreConstant.PARENT_INFO_KEYS.forEach { key ->
                    preferences.remove(key)
                }

                val dynamicPermissionKeys = preferences.asMap().keys.filter {
                    it.name.startsWith(DataStoreConstant.KEY_PERMISSION_DENIED_COUNT)
                }
                dynamicPermissionKeys.forEach { key ->
                    preferences.remove(key)
                }

                DataStoreConstant.TOKEN_KEYS.forEach { key ->
                    preferences.remove(key)
                }
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

    override suspend fun saveTermsInfo(isRequiredTermsAllAgreed: Boolean) {
        suspendRunCatching {
            dataStore.edit { preferences ->
                preferences[KEY_IS_REQUIRED_TERMS_ALL_AGREED] = isRequiredTermsAllAgreed
            }
        }.onFailure {
            Timber.e(it, "Failed to save terms info")
        }
    }

    override suspend fun getTermsInfo(): Boolean? {
        return suspendRunCatching {
            dataStore.data.map { preferences ->
                preferences[KEY_IS_REQUIRED_TERMS_ALL_AGREED]
            }.first()
        }.onFailure {
            Timber.e(it, "Failed to get terms info")
        }.getOrNull()
    }

    override suspend fun saveAgreedTermsIds(termsIds: List<Long>) {
        suspendRunCatching {
            dataStore.edit { preferences ->
                preferences[KEY_AGREED_TERMS_IDS] = termsIds.joinToString(",")
            }
        }.onFailure {
            Timber.e(it, "Failed to save agreed terms ids")
        }
    }

    override suspend fun getAgreedTermsIds(): List<Long>? {
        return suspendRunCatching {
            dataStore.data.map { preferences ->
                val agreedTermsIds = preferences[KEY_AGREED_TERMS_IDS]
                agreedTermsIds?.split(",")?.map { it.toLong() }
            }.first()
        }.onFailure {
            Timber.e(it, "Failed to get agreed terms ids")
        }.getOrNull()
    }

    override suspend fun saveChildName(lastName: String, firstName: String) {
        suspendRunCatching {
            dataStore.edit { preferences ->
                preferences[KEY_CHILD_LAST_NAME] = lastName
                preferences[KEY_CHILD_FIRST_NAME] = firstName
            }
        }.onFailure {
            Timber.e(it, "Failed to save child name")
        }
    }

    override suspend fun getChildLastName(): String? {
        return suspendRunCatching {
            dataStore.data.map { preferences ->
                preferences[KEY_CHILD_LAST_NAME]
            }.first()
        }.onFailure {
            Timber.e(it, "Failed to get child last name")
        }.getOrNull()
    }

    override suspend fun getChildFirstName(): String? {
        return suspendRunCatching {
            dataStore.data.map { preferences ->
                preferences[KEY_CHILD_FIRST_NAME]
            }.first()
        }.onFailure {
            Timber.e(it, "Failed to get child first name")
        }.getOrNull()
    }

    override suspend fun savePendingInviteCode(code: String, expireTimeMillis: Long) {
        suspendRunCatching {
            dataStore.edit { preferences ->
                preferences[KEY_PENDING_INVITE_CODE] = code
                preferences[KEY_PENDING_INVITE_EXPIRE_TIME] = expireTimeMillis
            }
        }.onFailure {
            Timber.e(it, "Failed to save pending invite code")
        }
    }

    override suspend fun getPendingInviteCode(): String? {
        return suspendRunCatching {
            dataStore.data.map { preferences ->
                preferences[KEY_PENDING_INVITE_CODE]
            }.first()
        }.onFailure {
            Timber.e(it, "Failed to get pending invite code")
        }.getOrNull()
    }

    override suspend fun getPendingInviteExpireTime(): Long {
        return suspendRunCatching {
            dataStore.data.map { preferences ->
                preferences[KEY_PENDING_INVITE_EXPIRE_TIME] ?: 0L
            }.first()
        }.getOrDefault(0L)
    }

    override suspend fun clearPendingInviteCode() {
        suspendRunCatching {
            dataStore.edit { preferences ->
                preferences.remove(KEY_PENDING_INVITE_CODE)
                preferences.remove(KEY_PENDING_INVITE_EXPIRE_TIME)
            }
        }.onFailure {
            Timber.e(it, "Failed to clear pending invite code")
        }
    }
}
