package com.illiarb.tmdbexplorer.coreui.observable

/**
 * @author ilya-rb on 17.01.19.
 */
class BypassObservable<T> : Observable<T> {

    private val observers = mutableSetOf<Observer<T>>()

    fun publish(value: T) {
        observers.forEach {
            it.onNewValue(value)
        }
    }

    override fun addObserver(observer: Observer<T>) {
        observers.add(observer)
    }

    override fun removeObserver(observer: Observer<T>) {
        observers.remove(observer)
    }
}