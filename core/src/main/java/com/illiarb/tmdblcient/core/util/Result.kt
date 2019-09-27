package com.illiarb.tmdblcient.core.util

sealed class Result<out T : Any> {

    class Success<out T : Any>(val data: T) : Result<T>()

    class Error(val error: Throwable) : Result<Nothing>()
}