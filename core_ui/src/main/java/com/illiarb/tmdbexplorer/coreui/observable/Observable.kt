package com.illiarb.tmdbexplorer.coreui.observable

/**
 * @author ilya-rb on 09.01.19.
 */
interface Observable<T> {

    fun addObserver(observer: Observer<T>)

    fun removeObserver(observer: Observer<T>)
}