package com.illiarb.tmdbexplorer.coreui.common

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow

interface OnClickListener {

    fun onClick(item: Any)

    fun clicks(): Flow<Any>

    class DefaultOnClickListener : OnClickListener {

        private val clicksChannel = Channel<Any>()

        override fun clicks(): Flow<Any> = clicksChannel.consumeAsFlow()

        override fun onClick(item: Any) {
            clicksChannel.offer(item)
        }
    }
}