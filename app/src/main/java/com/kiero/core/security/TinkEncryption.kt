package com.kiero.core.security

import android.content.Context
import android.util.Base64
import com.google.crypto.tink.Aead
import com.google.crypto.tink.InsecureSecretKeyAccess
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkJsonProtoKeysetFormat
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.PredefinedAeadParameters
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import java.io.File
import java.nio.charset.StandardCharsets

interface CryptoManager {
    fun encrypt(plaintext: String): String
    fun decrypt(ciphertext: String): String
}

class TinkCryptoManager @Inject constructor(
   @param:ApplicationContext private val context: Context
) : CryptoManager {

    private val aead: Aead

    init {
        try {
            AeadConfig.register()

            val keysetFile = File(context.filesDir, KEYSET_FILENAME)
            val keysetHandle = if (keysetFile.exists()) {
                loadKeysetHandle(keysetFile)
            } else {
                generateAndSaveKeysetHandle(keysetFile)
            }

            aead = keysetHandle.getPrimitive(Aead::class.java)
        } catch (e: Exception) {
            throw IllegalStateException("암호화 초기화 실패", e)
        }
    }

    private fun generateAndSaveKeysetHandle(keysetFile: File): KeysetHandle {
        return try {
            val keysetHandle = KeysetHandle.generateNew(PredefinedAeadParameters.AES256_GCM)
            val keysetJson = TinkJsonProtoKeysetFormat.serializeKeyset(
                keysetHandle,
                InsecureSecretKeyAccess.get()
            )
            keysetFile.writeText(keysetJson)
            keysetHandle
        } catch (e: Exception) {
            throw EncryptionException("키셋 생성 및 저장 실패", e)
        }
    }

    private fun loadKeysetHandle(keysetFile: File): KeysetHandle {
        return try {
            val keysetJson = keysetFile.readText()
            TinkJsonProtoKeysetFormat.parseKeyset(
                keysetJson,
                InsecureSecretKeyAccess.get()
            )
        } catch (e: Exception) {
            throw EncryptionException("키셋 로드 실패", e)
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
        private val CHARSET = StandardCharsets.UTF_8
        private const val BASE64_FLAGS = Base64.NO_WRAP
        private val ASSOCIATED_DATA = null
    }

    class EncryptionException(message: String, cause: Throwable? = null) :
        Exception(message, cause)
}