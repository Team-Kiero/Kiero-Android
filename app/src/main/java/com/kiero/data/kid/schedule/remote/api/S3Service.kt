package com.kiero.data.kid.schedule.remote.api

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Url

interface S3Service {
    @PUT
    suspend fun uploadImageToS3(
        @Url url: String,
        @Body image: RequestBody
    ): Response<Unit>
}