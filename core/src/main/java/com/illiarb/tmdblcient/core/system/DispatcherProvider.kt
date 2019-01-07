package com.illiarb.tmdblcient.core.system

import kotlinx.coroutines.CoroutineDispatcher

/**
 * @author ilya-rb on 07.01.19.
 */
interface DispatcherProvider {

    val ioDispatcher: CoroutineDispatcher

    val mainDispatcher: CoroutineDispatcher

    val defaultDispatcher: CoroutineDispatcher
}