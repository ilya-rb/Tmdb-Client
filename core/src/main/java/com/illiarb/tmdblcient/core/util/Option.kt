package com.illiarb.tmdblcient.core.util

sealed class Option<T : Any> {

    class Some<T : Any>(val value: T) : Option<T>()

    class None<T : Any> : Option<T>()
}