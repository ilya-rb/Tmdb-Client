package com.illiarb.tmdbexplorer.coreui.widget

import android.view.View
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlin.coroutines.CoroutineContext

interface UiFlowWidget : CoroutineScope {

    val clicks: Flow<Unit>

    fun onDetachedFromWindow()

    @FlowPreview
    class UiFlowDelegate(private val view: View) : UiFlowWidget, CoroutineScope {

        private val clicksChannel = Channel<Unit>()
        private val job = Job()

        override val coroutineContext: CoroutineContext
            get() = job + Dispatchers.Main

        override val clicks: Flow<Unit>
            get() {
                view.setOnClickListener {
                    launch {
                        clicksChannel.send(Unit)
                    }
                }
                return clicksChannel.consumeAsFlow()
            }

        override fun onDetachedFromWindow() = job.cancelChildren()
    }
}