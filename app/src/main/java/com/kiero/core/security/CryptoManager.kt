package com.kiero.core.security

interface CryptoManager {
    fun encrypt(plaintext: String): String
    fun decrypt(ciphertext: String): String
}