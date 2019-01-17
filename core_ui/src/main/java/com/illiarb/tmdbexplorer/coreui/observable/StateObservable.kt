package com.illiarb.tmdbexplorer.coreui.observable

import kotlin.properties.Delegates

/**
 * @author ilya-rb on 17.01.19.
 */
class StateObservable<T : Cloneable<T>> : Observable<T> {

    private val observers = mutableSetOf<Observer<T>>()

    private var value: T? by Delegates.observable<T?>(null) { _, _, newValue: T? ->
        newValue?.let {
            notifyObservers(it, *observers.toTypedArray())
        }
    }

    fun accept(value: T) {
        this.value = value
    }

    fun valueCopy(): T? = value?.clone()

    override fun addObserver(observer: Observer<T>) {
        observers.add(observer)

        // Notify if we already have some value
        value?.let {
            notifyObservers(it, observer)
        }
    }

    override fun removeObserver(observer: Observer<T>) {
        observers.remove(observer)
    }

    private fun notifyObservers(value: T, vararg observers: Observer<T>) {
        observers.forEach { observer ->
            observer.onNewValue(value)
        }
    }
}