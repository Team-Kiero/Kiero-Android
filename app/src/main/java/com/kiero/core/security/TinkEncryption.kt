package com.kiero.core.security

import android.content.Context
import android.util.Base64
import com.google.crypto.tink.Aead
import com.google.crypto.tink.InsecureSecretKeyAccess
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkJsonProtoKeysetFormat
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.PredefinedAeadParameters
import java.io.File
import java.nio.charset.StandardCharsets

/**
 * Tink 기반 암호화/복호화 유틸리티
 */
class TinkEncryption(private val context: Context) {

    companion object {
        private const val KEYSET_FILENAME = "kiero_tink_keyset.json"
    }
    private val aead: Aead
    init {
        // 1. Tink AEAD 구성 등록
        AeadConfig.register()

        // 2. 키셋 파일 경로
        val keysetFile = File(context.filesDir, KEYSET_FILENAME)

        // 3. KeysetHandle 생성 또는 로드
        val keysetHandle = if (keysetFile.exists()) {
            // 기존 키가 있으면 로드
            loadKeysetHandle(keysetFile)
        } else {
            // 없으면 새로 생성
            generateAndSaveKeysetHandle(keysetFile)
        }

        // 4. AEAD primitive 가져오기
        aead = keysetHandle.getPrimitive(Aead::class.java)
    }

    private fun generateAndSaveKeysetHandle(keysetFile: File): KeysetHandle {
        // AES-256-GCM 키 생성
        val keysetHandle = KeysetHandle.generateNew(
            PredefinedAeadParameters.AES256_GCM
        )

        // JSON 형식으로 직렬화 (InsecureSecretKeyAccess 필요)
        val keysetJson = TinkJsonProtoKeysetFormat.serializeKeyset(
            keysetHandle,
            InsecureSecretKeyAccess.get()
        )

        // 파일에 저장
        keysetFile.writeText(keysetJson)

        return keysetHandle
    }
    private fun loadKeysetHandle(keysetFile: File): KeysetHandle {
        // 파일에서 JSON 읽기
        val keysetJson = keysetFile.readText()

        // JSON을 KeysetHandle로 파싱
        return TinkJsonProtoKeysetFormat.parseKeyset(
            keysetJson,
            InsecureSecretKeyAccess.get()
        )
    }

    fun encrypt(plaintext: String): String {
        // 1. 문자열 → 바이트 배열
        val plaintextBytes = plaintext.toByteArray(StandardCharsets.UTF_8)

        // 2. AEAD 암호화 (associatedData = null)
        //    associatedData: 암호화되지 않지만 인증되는 데이터 (옵션)
        val encryptedBytes = aead.encrypt(plaintextBytes, null)

        // 3. Base64 인코딩
        return Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
    }

    fun decrypt(ciphertext: String): String {
        // 1. Base64 디코딩
        val encryptedBytes = Base64.decode(ciphertext, Base64.NO_WRAP)

        // 2. AEAD 복호화
        val decryptedBytes = aead.decrypt(encryptedBytes, null)

        // 3. 바이트 배열 → 문자열
        return String(decryptedBytes, StandardCharsets.UTF_8)
    }
}