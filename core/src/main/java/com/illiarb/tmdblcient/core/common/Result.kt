package com.illiarb.tmdblcient.core.common

/**
 * @author ilya-rb on 09.01.19.
 */
sealed class Result<out T : Any> {
    data class Success<out T : Any>(val result: T) : Result<T>()
    data class Error<out T : Any>(val error: Throwable) : Result<T>()
}

inline fun <T : Any> invokeForResult(block: () -> T): Result<T> {
    return try {
        Result.Success(block())
    } catch (e: Exception) {
        Result.Error(e)
    }
}