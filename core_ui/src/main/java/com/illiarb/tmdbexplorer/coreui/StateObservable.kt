package com.illiarb.tmdbexplorer.coreui

/**
 * @author ilya-rb on 09.01.19.
 */
class SimpleStateObservable<T : Cloneable<T>> : StateObservable<T> {

    private val observers = mutableSetOf<StateObserver<T>>()

    private var value: T? = null

    fun accept(value: T) {
        this.value = value
        notifyObservers(value, *observers.toTypedArray())
    }

    fun hasValue(): Boolean = value != null

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
        // Make a copy so state can't modified
        // in any observer
        val copy = value.clone()
        if (copy === value) {
            throw IllegalArgumentException("clone() must provide a copy")
        }

        observers.forEach { observer ->
            observer.onStateChanged(copy)
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