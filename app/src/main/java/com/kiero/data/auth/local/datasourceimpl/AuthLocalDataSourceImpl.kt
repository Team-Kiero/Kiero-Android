package com.kiero.data.auth.local.datasourceimpl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kiero.core.common.util.TinkEncryption
import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.auth.local.datasource.AuthLocalDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * AuthLocalDataSource 구현체
 *
 * DataStore + Tink 암호화를 사용하여 JWT 토큰을 안전하게 저장
 *
 */
class AuthLocalDataSourceImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val tinkEncryption: TinkEncryption,
) : AuthLocalDataSource {

    companion object {
        private const val DATASTORE_NAME = "kiero_auth_datastore"

        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = DATASTORE_NAME
    )

    override suspend fun saveAccessToken(token: String) {
        suspendRunCatching {
            // 1. 암호화
            val encryptedToken = tinkEncryption.encrypt(token)

            // 2. DataStore 저장
            context.dataStore.edit { preferences ->
                preferences[KEY_ACCESS_TOKEN] = encryptedToken
            }
        }
    }

    override suspend fun saveRefreshToken(token: String) {
        suspendRunCatching {
            val encryptedToken = tinkEncryption.encrypt(token)

            context.dataStore.edit { preferences ->
                preferences[KEY_REFRESH_TOKEN] = encryptedToken
            }
        }
    }

    override suspend fun getAccessToken(): String? {
        return suspendRunCatching {
            context.dataStore.data
                .map { preferences -> preferences[KEY_ACCESS_TOKEN] }
                .first()
                ?.let { encryptedToken ->
                    // 복호화
                    tinkEncryption.decrypt(encryptedToken)
                }
        }.getOrNull()
    }

    override suspend fun getRefreshToken(): String? {
        return suspendRunCatching {
            context.dataStore.data
                .map { preferences -> preferences[KEY_REFRESH_TOKEN] }
                .first()
                ?.let { encryptedToken ->
                    tinkEncryption.decrypt(encryptedToken)
                }
        }.getOrNull()
    }
    
    override suspend fun clearTokens() {
        suspendRunCatching {
            context.dataStore.edit { preferences ->
                preferences.remove(KEY_ACCESS_TOKEN)
                preferences.remove(KEY_REFRESH_TOKEN)
            }
        }
    }
}