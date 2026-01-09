package com.kiero.core.network.extension

import com.kiero.core.network.model.ApiResult

inline fun <T, R> ApiResult<T>.toResult(
    transform: (T) -> R
): Result<R> {
    return when (this) {
        is ApiResult.Success -> {
            try {
                Result.success(transform(this.data))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
        is ApiResult.ApiError -> {
            Result.failure(Exception("Api Error: code=${this.code}, msg=${this.message}"))
        }
        is ApiResult.Failure -> {
            Result.failure(this.exception)
        }
    }
}
