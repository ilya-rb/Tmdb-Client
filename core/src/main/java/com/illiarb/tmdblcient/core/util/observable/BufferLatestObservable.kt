package com.illiarb.tmdblcient.core.util.observable

import com.illiarb.tmdblcient.core.util.Cloneable

/**
 * @author ilya-rb on 20.01.19.
 */
class BufferLatestObservable<T>(initial: T) : SimpleObservable<T>() {

    companion object {

        fun <T : Cloneable<T>> asImmutable(initial: T): ImmutableObservable<T> {
            return ImmutableObservable(initial, BufferLatestObservable(initial))
        }
    }

    private var buffer: T = initial

    override fun accept(value: T) {
        super.accept(value)
        buffer = value
    }

    override fun addObserver(observer: Observer<T>) {
        super.addObserver(observer)
        notifyObservers(buffer, observer)
    }
}