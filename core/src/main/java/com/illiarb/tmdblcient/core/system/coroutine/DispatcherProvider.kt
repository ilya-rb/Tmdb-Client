package com.illiarb.tmdblcient.core.system.coroutine

import kotlinx.coroutines.CoroutineDispatcher

/**
 * @author ilya-rb on 07.01.19.
 */
interface DispatcherProvider {

    val io: CoroutineDispatcher

    val main: CoroutineDispatcher

    val default: CoroutineDispatcher

    val none: CoroutineDispatcher
}