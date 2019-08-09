package com.illiarb.tmdblcient.core.ext

import kotlinx.coroutines.flow.FlowCollector

fun <T> emit(block: (T) -> Unit): FlowCollector<T> {
    return object : FlowCollector<T> {
        override suspend fun emit(value: T) {
            block(value)
        }
    }
}