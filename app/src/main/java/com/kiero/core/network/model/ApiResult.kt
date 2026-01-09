package com.kiero.core.network.model

sealed interface ApiResult<out T> {
    data class Success<T>(
        val data: T
    ) : ApiResult<T>

    data class ApiError(
        val code: String,
        val message: String
    ) : ApiResult<Nothing>

    data class Failure(
        val exception: Throwable
    ) : ApiResult<Nothing>
}
