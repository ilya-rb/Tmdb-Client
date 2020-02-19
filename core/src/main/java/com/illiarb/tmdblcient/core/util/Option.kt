package com.illiarb.tmdblcient.core.util

sealed class Option<T : Any> {

    class Some<T : Any>(val value: T) : Option<T>()

    class None<T : Any> : Option<T>()

    fun getOrElse(fallback: () -> T): T =
        when (this) {
            is Some -> value
            is None -> fallback()
        }

    fun invokeIfSome(block: (T) -> Unit) {
        if (this is Some) {
            block(value)
        }
    }
}