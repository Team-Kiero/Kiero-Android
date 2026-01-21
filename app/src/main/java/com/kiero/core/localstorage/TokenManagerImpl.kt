package com.kiero.core.localstorage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.kiero.core.common.util.suspendRunCatching
import com.kiero.core.localstorage.constant.DataStoreConstant.KEY_ACCESS_TOKEN
import com.kiero.core.localstorage.constant.DataStoreConstant.KEY_CHILD_ID
import com.kiero.core.localstorage.constant.DataStoreConstant.KEY_REFRESH_TOKEN
import com.kiero.core.localstorage.constant.DataStoreConstant.KEY_USER_ROLE
import com.kiero.core.model.auth.UserRole
import com.kiero.core.security.CryptoManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class TokenManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val cryptoManager: CryptoManager
) : TokenManager {
    @Volatile
    private var cachedAccessToken: String? = null

    override suspend fun saveAccessToken(token: String) {
        suspendRunCatching {
            cachedAccessToken = token
            val encrypted = cryptoManager.encrypt(token)

            dataStore.edit {
                it[KEY_ACCESS_TOKEN] = encrypted
            }
        }.onFailure { Timber.e(it, "AccessToken 저장 실패") }
    }

    override suspend fun saveRefreshToken(token: String) {
        suspendRunCatching {
            val encrypted = cryptoManager.encrypt(token)

            dataStore.edit {
                it[KEY_REFRESH_TOKEN] = encrypted
            }
        }.onFailure { Timber.e(it, "RefreshToken 저장 실패") }
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        suspendRunCatching {
            cachedAccessToken = accessToken
            val encryptedAccess = cryptoManager.encrypt(accessToken)
            val encryptedRefresh = cryptoManager.encrypt(refreshToken)

            dataStore.edit {
                it[KEY_ACCESS_TOKEN] = encryptedAccess
                it[KEY_REFRESH_TOKEN] = encryptedRefresh
            }
        }.onFailure { Timber.e(it, "토큰 일괄 저장 실패") }
    }

    override suspend fun getAccessToken(): String? {
        if (cachedAccessToken != null) {
            return cachedAccessToken
        }

        return suspendRunCatching {
            val encrypted = dataStore.data.map { it[KEY_ACCESS_TOKEN] }.first()
            encrypted?.let { cryptoManager.decrypt(it) }?.also {
                cachedAccessToken = it
            }
        }.onFailure { Timber.e(it, "AccessToken 로드 실패") }.getOrNull()
    }

    override suspend fun getRefreshToken(): String? {
        return suspendRunCatching {
            val encrypted = dataStore.data.map { it[KEY_REFRESH_TOKEN] }.first()
            encrypted?.let {
                cryptoManager.decrypt(it)
            }
        }.onFailure { Timber.e(it, "RefreshToken 로드 실패") }.getOrNull()
    }

    override suspend fun clearTokens() {
        suspendRunCatching {
            cachedAccessToken = null
            dataStore.edit { it.clear() }
        }
    }

    override suspend fun saveUserRole(role: UserRole) {
        suspendRunCatching {
            dataStore.edit {
                it[KEY_USER_ROLE] = role.name
            }
        }.onFailure { Timber.e(it, "UserRole 저장 실패") }
    }

    override suspend fun getUserRole(): UserRole? {
        return suspendRunCatching {
            val roleName = dataStore.data.map { it[KEY_USER_ROLE] }.first()
            roleName?.let { UserRole.valueOf(it) }
        }.onFailure { Timber.e(it, "UserRole 로드 실패") }.getOrNull()
    }
}