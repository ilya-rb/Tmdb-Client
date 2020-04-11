package com.illiarb.tmdbclient.libs.util

/**
 * Adapted from here:
 * https://github.com/airbnb/MvRx/blob/master/mvrx/src/main/kotlin/com/airbnb/mvrx/Async.kt
 */
sealed class Async<out T> {

  object Uninitialized : Async<Nothing>()

  class Loading<out T> : Async<T>()

  data class Success<out T>(private val value: T) : Async<T>() {
    override fun invoke(): T = value
  }

  data class Fail<out T>(val error: Throwable) : Async<T>()

  /**
   * Returns the Left value or null.
   *
   * Can be invoked as an operator like: `yourProp()`
   */
  open operator fun invoke(): T? = null

  inline fun doOnSuccess(block: (T) -> Unit) {
    if (this is Success) {
      block(this())
    }
  }

  inline fun doOnError(block: (Throwable) -> Unit) = this.also {
    if (this is Fail) {
      block(error)
    }
  }
}