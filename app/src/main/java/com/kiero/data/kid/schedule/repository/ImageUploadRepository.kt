package com.kiero.data.kid.schedule.repository

interface ImageUploadRepository {
    suspend fun uploadImage(
        uriString: String,
        fileName: String,
        contentType: String = "image/jpeg"
    ): Result<String>
}