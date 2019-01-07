package com.illiarb.tmdbexplorer.coreui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

/**
 * @author ilya-rb on 07.01.19.
 */
abstract class BaseViewModel : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = job + provideDefaultDispatcher()

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    abstract fun provideDefaultDispatcher(): CoroutineDispatcher
}