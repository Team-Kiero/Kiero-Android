package com.kiero.core.common.extension

import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

fun Throwable.toHandleErrorMessage(): String {
    return when (this) {
        is TimeoutException -> "네트워크 시간이 초과되었습니다. 다시 시도해주세요."
        is SocketTimeoutException -> "네트워크 시간이 초과되었습니다. 다시 시도해주세요"
        is UnknownHostException -> "서버에 연결할 수 없습니다. 인터넷 연결을 확인하세요."
        is IOException -> "네트워크 연결에 문제가 발생했습니다. 다시 시도하세요."
        else -> "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도하세요."
    }
}
