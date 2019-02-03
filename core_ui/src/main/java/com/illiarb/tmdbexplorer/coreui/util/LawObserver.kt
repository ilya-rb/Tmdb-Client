package com.illiarb.tmdbexplorer.coreui.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.illiarb.tmdblcient.core.util.observable.Observable
import com.illiarb.tmdblcient.core.util.observable.Observer

/**
 * Law - Lifecycle Aware Observer
 *
 * @author ilya-rb on 18.01.19.
 */
abstract class LawObserver<T>(private val observable: Observable<T>) : Observer<T>,
    LifecycleObserver {

    companion object {

        fun <T> create(observable: Observable<T>, block: (T) -> Unit): LawObserver<T> {
            return object : LawObserver<T>(observable) {
                override fun onNewValue(value: T) {
                    block(value)
                }
            }
        }
    }

    private var owner: LifecycleOwner? = null

    fun register(owner: LifecycleOwner) {
        this.owner = owner.also { it.lifecycle.addObserver(this) }
        observable.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun unregister() {
        this.owner?.lifecycle?.removeObserver(this)
        this.owner = null
        observable.removeObserver(this)
    }

}