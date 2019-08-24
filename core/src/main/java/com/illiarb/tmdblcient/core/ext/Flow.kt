package com.illiarb.tmdblcient.core.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun <T> Flow<T>.collectWithScope(
    coroutineScope: CoroutineScope,
    collector: suspend CoroutineScope.(T) -> Unit
) {
    coroutineScope.launch {
        collect { collector(it) }
    }
}

inline fun <T> CoroutineScope.launchAndCollect(
    flow: Flow<T>,
    crossinline block: suspend CoroutineScope.(T) -> Unit
) {
    launch {
        flow.collect { block(it) }
    }
}