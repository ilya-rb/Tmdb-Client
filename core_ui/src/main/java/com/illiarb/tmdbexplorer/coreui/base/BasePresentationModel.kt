package com.illiarb.tmdbexplorer.coreui.base

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbexplorer.coreui.Cloneable
import com.illiarb.tmdbexplorer.coreui.SimpleStateObservable
import com.illiarb.tmdbexplorer.coreui.StateObservable
import com.illiarb.tmdbexplorer.coreui.StateObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * @author ilya-rb on 07.01.19.
 */
abstract class BasePresentationModel<T : Cloneable<T>> : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()

    private val stateObservable = SimpleStateObservable<T>()

    protected fun setState(state: T) {
        stateObservable.accept(state)
    }

    fun observeState(observer: StateObserver<T>) {
        stateObservable.addObserver(observer)
    }

    fun stopObserving(observer: StateObserver<T>) {
        stateObservable.removeObserver(observer)
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}