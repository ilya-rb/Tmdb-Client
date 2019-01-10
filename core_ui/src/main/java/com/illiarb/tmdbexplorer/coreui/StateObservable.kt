package com.illiarb.tmdbexplorer.coreui

import kotlin.properties.Delegates

/**
 * @author ilya-rb on 09.01.19.
 */
class SimpleStateObservable<T : Cloneable<T>> : StateObservable<T> {

    private val observers = mutableSetOf<StateObserver<T>>()

    private var value: T? by Delegates.observable<T?>(null) { _, _, newValue: T? ->
        newValue?.let {
            // Make a copy so state can't modified
            // in any observer
            notifyObservers(it.clone(), *observers.toTypedArray())
        }
    }

    fun accept(value: T) {
        this.value = value
    }

    fun valueCopy(): T? = value?.clone()

    override fun addObserver(observer: StateObserver<T>) {
        observers.add(observer)

        // Notify if we already have some value
        value?.let {
            notifyObservers(it, observer)
        }
    }

    override fun removeObserver(observer: StateObserver<T>) {
        observers.remove(observer)
    }

    private fun notifyObservers(value: T, vararg observers: StateObserver<T>) {
        observers.forEach { observer ->
            observer.onStateChanged(value)
        }
    }
}

interface Cloneable<T> {
    fun clone(): T
}

interface StateObservable<T> {

    fun addObserver(observer: StateObserver<T>)

    fun removeObserver(observer: StateObserver<T>)
}

interface StateObserver<T> {

    fun onStateChanged(state: T)
}