package com.Kiero.core.common.util

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.coroutineContext

suspend fun <R> suspendRunCatching(block: suspend () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (t: TimeoutCancellationException) {
        Result.failure(t)
    } catch (c: CancellationException) {
        throw c
    } catch (e: Throwable) {
        coroutineContext.ensureActive()
        Result.failure(e)
    }
}