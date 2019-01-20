package com.illiarb.tmdblcient.core.util.observable

/**
 * @author ilya-rb on 20.01.19.
 */
interface Observable <T> {

    fun addObserver(observer: Observer<T>)

    fun removeObserver(observer: Observer<T>)

    fun accept(value: T)
}