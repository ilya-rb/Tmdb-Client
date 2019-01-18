package com.illiarb.tmdbexplorer.coreui.observable

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * @author ilya-rb on 18.01.19.
 */
abstract class LifecycleAwareObserver<T>(private val observable: Observable<T>) : Observer<T>, LifecycleObserver {

    private var owner: LifecycleOwner? = null

    fun register(owner: LifecycleOwner) {
        this.owner = owner.also { it.lifecycle.addObserver(this) }
        observable.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun unregister() {
        this.owner?.lifecycle?.removeObserver(this)
        observable.removeObserver(this)
    }

}