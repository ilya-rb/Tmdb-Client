package com.illiarb.tmdbexplorer.coreui.base

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbexplorer.coreui.uiactions.ShowErrorDialogAction
import com.illiarb.tmdbexplorer.coreui.uiactions.UiAction
import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.util.Cloneable
import com.illiarb.tmdblcient.core.util.observable.BufferLatestObservable
import com.illiarb.tmdblcient.core.util.observable.ImmutableObservable
import com.illiarb.tmdblcient.core.util.observable.Observable
import com.illiarb.tmdblcient.core.util.observable.SimpleObservable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * @author ilya-rb on 07.01.19.
 */
typealias ErrorHandler = (Throwable) -> Unit

typealias SuccessHandler <T> = (T) -> Unit

abstract class BasePresentationModel<T : Cloneable<T>> : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()

    private val stateObservable = ImmutableObservable<T>(BufferLatestObservable())

    private val actionsObservable = SimpleObservable<UiAction>()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun actionsObservable(): SimpleObservable<UiAction> = actionsObservable

    fun stateObservable(): Observable<T> = stateObservable

    protected fun <T> handleResult(
        result: Result<T>,
        onSuccess: SuccessHandler<T>,
        onError: ErrorHandler? = null
    ) {
        when (result) {
            is Result.Success -> onSuccess(result.result)
            is Result.Error -> {
                val throwable = result.error

                if (onError != null) {
                    onError(throwable)
                } else {
                    handleError(throwable)
                }
            }
        }
    }

    protected fun handleError(throwable: Throwable) {
        throwable.message?.let {
            publishAction(ShowErrorDialogAction(it))
        }
    }

    protected fun runCoroutine(block: suspend CoroutineScope.() -> Unit) {
        launch(context = coroutineContext) {
            block(this)
        }
    }

    protected fun setIdleState(state: T) {
        stateObservable.accept(state)
    }

    protected fun setState(block: (T) -> T) {
        stateObservable.accept(block(stateObservable.requireValue()))
    }

    protected fun publishAction(action: UiAction) {
        actionsObservable.accept(action)
    }
}