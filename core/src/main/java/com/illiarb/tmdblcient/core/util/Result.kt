package com.illiarb.tmdblcient.core.util

sealed class Result<out T : Any> {

    class Success<out T : Any>(val data: T) : Result<T>()

    class Error(val error: Throwable) : Result<Nothing>()

    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Error -> throw error
    }

    fun asAsync(): Async<T> = when (this) {
        is Success -> Async.Success(data)
        is Error -> Async.Fail(error)
    }

    companion object {

        suspend fun <T : Any> create(block: suspend () -> T): Result<T> {
            return try {
                Success(block())
            } catch (e: Exception) {
                Error(e)
            }
        }
    }
}