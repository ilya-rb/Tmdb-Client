package com.illiarb.tmdblcient.core.util.observable

/**
 * @author ilya-rb on 20.01.19.
 */
open class SimpleObservable<T> : Observable<T> {

    private val observers = mutableSetOf<Observer<T>>()

    override fun addObserver(observer: Observer<T>) {
        observers.add(observer)
    }

    override fun accept(value: T) {
        notifyObservers(value, *observers.toTypedArray())
    }

    override fun removeObserver(observer: Observer<T>) {
        observers.remove(observer)
    }

    protected fun observersCount(): Int = observers.size

    protected fun notifyObservers(value: T, vararg observers: Observer<T>) {
        observers.forEach {
            it.onNewValue(value)
        }
    }
}