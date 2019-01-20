package com.illiarb.tmdblcient.core.util.observable

/**
 * @author ilya-rb on 20.01.19.
 */
class BufferLatestObservable<T> : SimpleObservable<T>() {

    private var buffer: T? = null

    override fun accept(value: T) {
        super.accept(value)
        buffer = value
    }

    override fun addObserver(observer: Observer<T>) {
        super.addObserver(observer)

        buffer?.let {
            notifyObservers(it, observer)
        }
    }
}