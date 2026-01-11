package com.kiero.data.auth.local.datasourceimpl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kiero.core.common.util.suspendRunCatching
import com.kiero.core.security.CryptoManager
import com.kiero.data.auth.local.datasource.AuthLocalDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class AuthLocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cryptoManager: CryptoManager,
) : AuthLocalDataSource {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = DATASTORE_NAME
    )

    override suspend fun saveAccessToken(token: String) {
        suspendRunCatching {
            val encryptedToken = cryptoManager.encrypt(token)
            context.dataStore.edit { it[KEY_ACCESS_TOKEN] = encryptedToken }
        }.onFailure { throwable ->
            Timber.e(throwable, "AccessToken 저장 실패")
        }
    }

    override suspend fun saveRefreshToken(token: String) {
        suspendRunCatching {
            val encryptedToken = cryptoManager.encrypt(token)
            context.dataStore.edit { it[KEY_REFRESH_TOKEN] = encryptedToken }
        }.onFailure { throwable ->
            Timber.e(throwable, "RefreshToken 저장 실패")
        }
    }

    override suspend fun getAccessToken(): String? {
        return suspendRunCatching {
            val encryptedToken = context.dataStore.data
                .map { it[KEY_ACCESS_TOKEN] }
                .first()
            encryptedToken?.let { cryptoManager.decrypt(it) }
        }.onFailure { throwable ->
            Timber.e(throwable, "AccessToken 로드 실패")
        }.getOrNull()
    }

    override suspend fun getRefreshToken(): String? {
        return suspendRunCatching {
            val encryptedToken = context.dataStore.data
                .map { it[KEY_REFRESH_TOKEN] }
                .first()
            encryptedToken?.let { cryptoManager.decrypt(it) }
        }.onFailure { throwable ->
            Timber.e(throwable, "RefreshToken 로드 실패")
        }.getOrNull()
    }

    override suspend fun clearTokens() {
        suspendRunCatching {
            context.dataStore.edit { it.clear() }
        }.onFailure { throwable ->
            Timber.e(throwable, "토큰 삭제 실패")
        }
    }

    companion object {
        private const val DATASTORE_NAME = "kiero_auth_datastore"
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }
}