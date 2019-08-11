package com.illiarb.tmdblcient.core.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

inline fun <T> CoroutineScope.launchAndCollect(flow: Flow<T>, crossinline block: suspend CoroutineScope.(T) -> Unit) {
    launch {
        flow.collect { block(it) }
    }
}