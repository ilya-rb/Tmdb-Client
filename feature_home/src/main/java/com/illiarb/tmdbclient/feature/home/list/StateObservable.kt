package com.illiarb.tmdbclient.feature.home.list

/**
 * @author ilya-rb on 09.01.19.
 */
class SimpleStateObservable<T> : StateObservable<T> {

    private val observers = mutableSetOf<StateObserver<T>>()

    private var value: T? = null

    fun accept(value: T) {
        this.value = value

        observers.forEach {
            it.onStateChanged(value)
        }
    }

    override fun addObserver(observer: StateObserver<T>) {
        observers.add(observer)

        value?.let {
            observer.onStateChanged(it)
        }
    }

    override fun removeObserver(observer: StateObserver<T>) {
        observers.remove(observer)
    }
}

interface StateObservable<T> {
    fun addObserver(observer: StateObserver<T>)
    fun removeObserver(observer: StateObserver<T>)
}

interface StateObserver<T> {
    fun onStateChanged(state: T)
}