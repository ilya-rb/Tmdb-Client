package com.illiarb.tmdblcient.core.util

sealed class Result<out T : Any> {

  class Success<out T : Any>(val data: T) : Result<T>()

  class Error(val error: Throwable) : Result<Nothing>()

  fun getOrThrow(): T = when (this) {
    is Success -> data
    is Error -> throw error
  }

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
      } catch (e: Exception) {
        Error(e)
      }
    }
  }
}