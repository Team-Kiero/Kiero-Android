package com.kiero.core.network.util

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.core.network.model.ApiResult
import com.kiero.core.network.model.BaseResponse

suspend inline fun <T : Any> safeApiCall(
    crossinline call: suspend () -> BaseResponse<T>
): ApiResult<T> {
    return suspendRunCatching {
        call.invoke()
    }.fold(
        onSuccess = { response ->
            val codeInt = response.code.toIntOrNull() ?: -1

            if (codeInt in 200..299) {
                ApiResult.Success(
                    data = response.data
                )
            } else {
                ApiResult.ApiError(
                    code = response.code,
                    message = response.message
                )
            }
        },
        onFailure = { exception ->
            ApiResult.Failure(exception)
        }
    )
}

