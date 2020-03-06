package com.illiarb.tmdblcient.core.util

import kotlinx.coroutines.CancellationException

sealed class Result<out T : Any> {

  class Success<out T : Any>(val data: T) : Result<T>()

  class Error(val error: Throwable) : Result<Nothing>()

  fun getOrThrow(): T = when (this) {
    is Success -> data
    is Error -> throw error
  }

  fun error(): Throwable = (this as Error).error

  inline fun <R : Any> mapOnSuccess(block: (T) -> R): Result<R> {
    return when (this) {
      is Success -> Success(block(data))
      is Error -> this
    }
  }

  fun asAsync(): Async<T> = when (this) {
    is Success -> Async.Success(data)
    is Error -> Async.Fail(error)
  }

  companion object {

    @Suppress("TooGenericExceptionCaught")
    inline fun <T : Any> create(block: () -> T): Result<T> {
      return try {
        Success(block())
      } catch (e: CancellationException) {
        throw e
      } catch (e: Exception) {
        Error(e)
      }
    }
  }
}