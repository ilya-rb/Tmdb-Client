package com.illiarb.tmdbclient.util

import kotlinx.coroutines.CancellationException

sealed class Result<out T> {

  class Ok<T>(val data: T) : Result<T>()

  class Err(val error: Throwable) : Result<Nothing>()

  fun unwrap(): T = when (this) {
    is Ok -> data
    is Err -> throw error
  }

  fun doIfOk(block: (T) -> Unit) {
    if (this is Ok) {
      block(data)
    }
  }

  fun error(): Throwable = (this as Err).error

  inline fun <R : Any> mapOnSuccess(block: (T) -> R): Result<R> {
    return when (this) {
      is Ok -> Ok(block(data))
      is Err -> this
    }
  }

  fun asAsync(): Async<T> = when (this) {
    is Ok -> Async.Success(data)
    is Err -> Async.Fail(error)
  }

  companion object {

    @Suppress("TooGenericExceptionCaught")
    inline fun <T : Any> create(block: () -> T): Result<T> {
      return try {
        Ok(block())
      } catch (e: CancellationException) {
        // Throw further to cancel coroutine
        throw e
      } catch (e: Throwable) {
        Err(e)
      }
    }
  }
}