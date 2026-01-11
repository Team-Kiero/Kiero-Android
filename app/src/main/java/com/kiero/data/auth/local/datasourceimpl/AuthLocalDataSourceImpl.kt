package com.kiero.data.auth.local.datasourceimpl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kiero.core.common.security.TinkEncryption
import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.auth.local.datasource.AuthLocalDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
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
            Timber.d("💾 AccessToken 암호화 및 저장 시작")
            val encryptedToken = tinkEncryption.encrypt(token)
            context.dataStore.edit { it[KEY_ACCESS_TOKEN] = encryptedToken }
        }.onSuccess {
            Timber.i("✅ AccessToken 저장 완료")
        }.onFailure {
            Timber.e(it, "❌ AccessToken 저장 중 에러")
        }
    }

    override suspend fun saveRefreshToken(token: String) {
        suspendRunCatching {
            Timber.d("💾 RefreshToken 암호화 및 저장 시작")
            val encryptedToken = tinkEncryption.encrypt(token)
            context.dataStore.edit { it[KEY_REFRESH_TOKEN] = encryptedToken }
        }.onSuccess {
            Timber.i("✅ RefreshToken 저장 완료")
        }.onFailure {
            Timber.e(it, "❌ RefreshToken 저장 중 에러")
        }
    }

    override suspend fun getAccessToken(): String? = suspendRunCatching {
        Timber.d("🔑 AccessToken 로드 및 복호화 시도")
        val encryptedToken = context.dataStore.data.map { it[KEY_ACCESS_TOKEN] }.first()
        encryptedToken?.let { tinkEncryption.decrypt(it) }
    }.onSuccess {
        if (it != null) Timber.i("✅ AccessToken 복호화 성공")
        else Timber.w("⚠️ 저장된 AccessToken이 없음")
    }.onFailure {
        Timber.e(it, "❌ AccessToken 로드 중 에러")
    }.getOrNull()

    override suspend fun getRefreshToken(): String? = suspendRunCatching {
        Timber.d("🔑 RefreshToken 로드 및 복호화 시도")
        val encryptedToken = context.dataStore.data
            .map { preferences -> preferences[KEY_REFRESH_TOKEN] }
            .first()
        encryptedToken?.let {
            tinkEncryption.decrypt(it)
        }
    }.onSuccess { token ->
        if (token != null) {
            Timber.i("✅ RefreshToken 복호화 성공")
        } else {
            Timber.w("⚠️ 저장된 RefreshToken이 없습니다.")
        }
    }.onFailure { throwable ->
        Timber.e(throwable, "❌ RefreshToken 로드 중 에러 발생")
    }.getOrNull()

    override suspend fun clearTokens() {
        suspendRunCatching {
            Timber.d("🧹 모든 토큰 삭제 시작")
            context.dataStore.edit { it.clear() }
        }.onSuccess {
            Timber.i("✅ 모든 토큰 삭제 완료")
        }.onFailure {
            Timber.e(it, "❌ 토큰 삭제 실패")
        }
    }
}