package com.kiero.core.security

import android.content.Context
import android.util.Base64
import com.google.crypto.tink.Aead
import com.google.crypto.tink.InsecureSecretKeyAccess
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkJsonProtoKeysetFormat
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.PredefinedAeadParameters
import com.google.crypto.tink.integration.android.AndroidKeystoreKmsClient
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import timber.log.Timber
import java.io.File
import java.nio.charset.StandardCharsets


class TinkCryptoManager @Inject constructor(
   @param:ApplicationContext private val context: Context
) : CryptoManager {

    private val aead: Aead

    init {
        try {
            AeadConfig.register()

            val masterKeyAead = getOrCreateMasterKeyAead()
            val keysetFile = File(context.filesDir, KEYSET_FILENAME)
            val keysetHandle = if (keysetFile.exists()) {
                loadOrMigrateKeysetHandle(keysetFile, masterKeyAead)
            } else {
                generateAndSaveKeysetHandle(keysetFile, masterKeyAead)
            }

            aead = keysetHandle.getPrimitive(Aead::class.java)
        } catch (e: Exception) {
            throw IllegalStateException("암호화 초기화 실패", e)
        }
    }

    private fun getOrCreateMasterKeyAead(): Aead {
        val keyUri = AndroidKeystoreKmsClient.PREFIX + MASTER_KEY_ALIAS
        return AndroidKeystoreKmsClient.getOrGenerateNewAeadKey(keyUri)
    }

    private fun loadOrMigrateKeysetHandle(keysetFile: File, masterKeyAead: Aead): KeysetHandle {
        return try {
            loadWrappedKeysetHandle(keysetFile, masterKeyAead)
        } catch (wrappedReadError: Exception) {
            Timber.d(wrappedReadError, "Keystore로 감싸진 키셋이 아님 - 레거시 마이그레이션 시도")
            try {
                migrateLegacyKeysetHandle(keysetFile, masterKeyAead)
            } catch (legacyReadError: Exception) {
                Timber.e(legacyReadError, "레거시 키셋 마이그레이션 실패 - 키셋 재발급(재로그인 필요)")
                keysetFile.delete()
                generateAndSaveKeysetHandle(keysetFile, masterKeyAead)
            }
        }
    }

    private fun loadWrappedKeysetHandle(keysetFile: File, masterKeyAead: Aead): KeysetHandle {
        val encryptedKeysetJson = keysetFile.readText()
        return TinkJsonProtoKeysetFormat.parseEncryptedKeyset(
            encryptedKeysetJson,
            masterKeyAead,
            KEYSET_ASSOCIATED_DATA
        )
    }

    private fun migrateLegacyKeysetHandle(keysetFile: File, masterKeyAead: Aead): KeysetHandle {
        val legacyKeysetHandle = try {
            val keysetJson = keysetFile.readText()
            TinkJsonProtoKeysetFormat.parseKeyset(keysetJson, InsecureSecretKeyAccess.get())
        } catch (e: Exception) {
            throw EncryptionException("레거시 키셋 로드 실패", e)
        }

        saveWrappedKeysetHandle(keysetFile, legacyKeysetHandle, masterKeyAead)
        return legacyKeysetHandle
    }

    private fun generateAndSaveKeysetHandle(keysetFile: File, masterKeyAead: Aead): KeysetHandle {
        return try {
            val keysetHandle = KeysetHandle.generateNew(PredefinedAeadParameters.AES256_GCM)
            saveWrappedKeysetHandle(keysetFile, keysetHandle, masterKeyAead)
            keysetHandle
        } catch (e: Exception) {
            throw EncryptionException("키셋 생성 및 저장 실패", e)
        }
    }

    private fun saveWrappedKeysetHandle(
        keysetFile: File,
        keysetHandle: KeysetHandle,
        masterKeyAead: Aead
    ) {
        try {
            val encryptedKeysetJson = TinkJsonProtoKeysetFormat.serializeEncryptedKeyset(
                keysetHandle,
                masterKeyAead,
                KEYSET_ASSOCIATED_DATA
            )

            // 임시 파일에 먼저 쓰고 교체해 마이그레이션 도중 프로세스가 죽어도
            // 기존 keyset 파일이 손상되지 않도록 한다.
            val tempFile = File(keysetFile.parentFile, "${keysetFile.name}.tmp")
            tempFile.writeText(encryptedKeysetJson)
            if (!tempFile.renameTo(keysetFile)) {
                throw EncryptionException("키셋 파일 교체 실패")
            }
        } catch (e: Exception) {
            throw EncryptionException("키셋 저장 실패", e)
        }
    }

    override fun encrypt(plaintext: String): String {
        return try {
            val plaintextBytes = plaintext.toByteArray(CHARSET)
            val encryptedBytes = aead.encrypt(plaintextBytes, ASSOCIATED_DATA)
            Base64.encodeToString(encryptedBytes, BASE64_FLAGS)
        } catch (e: Exception) {
            throw EncryptionException("암호화 실패", e)
        }
    }

    override fun decrypt(ciphertext: String): String {
        return try {
            val encryptedBytes = Base64.decode(ciphertext, BASE64_FLAGS)
            val decryptedBytes = aead.decrypt(encryptedBytes, ASSOCIATED_DATA)
            String(decryptedBytes, CHARSET)
        } catch (e: Exception) {
            throw EncryptionException("복호화 실패", e)
        }
    }

    companion object {
        private const val KEYSET_FILENAME = "kiero_tink_keyset.json"
        private const val MASTER_KEY_ALIAS = "kiero_tink_master_key"
        private val KEYSET_ASSOCIATED_DATA = ByteArray(0)
        private val CHARSET = StandardCharsets.UTF_8
        private const val BASE64_FLAGS = Base64.NO_WRAP
        private val ASSOCIATED_DATA = null
    }
}
