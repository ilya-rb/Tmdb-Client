package com.illiarb.tmdbexplorer.coreui.widget

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlin.coroutines.CoroutineContext

class CoroutineViewScopeDelegate : CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    fun onDetachedFromWindow() = job.cancelChildren()

}