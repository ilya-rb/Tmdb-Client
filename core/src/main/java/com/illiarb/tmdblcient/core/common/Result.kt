package com.illiarb.tmdblcient.core.common

import com.illiarb.tmdblcient.core.storage.ErrorHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

/**
 * @author ilya-rb on 09.01.19.
 */
sealed class Result<out T> {

    companion object {

        @Suppress("TooGenericExceptionCaught")
        suspend fun <T : Any> create(
            errorHandler: ErrorHandler,
            block: suspend CoroutineScope.() -> T
        ): Result<T> = coroutineScope {
            try {
                Success(block(this))
            } catch (e: Exception) {
                Error<T>(errorHandler.createExceptionFromThrowable(e))
            }
        }
    }

    data class Success<out T : Any>(val result: T) : Result<T>()

    data class Error<out T : Any>(val error: Throwable) : Result<T>()

    inline fun doOnSuccess(block: (T) -> Unit): Result<T> =
        this.also {
            if (this is Result.Success) {
                block(this.result)
            }
        }

    inline fun doOnError(block: (Throwable) -> Unit): Result<T> =
        this.also {
            if (this is Result.Error) {
                block(this.error)
            }
        }
}