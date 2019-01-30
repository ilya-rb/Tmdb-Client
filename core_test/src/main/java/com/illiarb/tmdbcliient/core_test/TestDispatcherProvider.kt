package com.illiarb.tmdbcliient.core_test

import com.illiarb.tmdblcient.core.system.coroutine.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * @author ilya-rb on 30.01.19.
 */
@ExperimentalCoroutinesApi
class TestDispatcherProvider : DispatcherProvider {

    override val io: CoroutineDispatcher = Dispatchers.Unconfined

    override val main: CoroutineDispatcher = Dispatchers.Unconfined

    override val default: CoroutineDispatcher = Dispatchers.Unconfined

    override val none: CoroutineDispatcher = Dispatchers.Unconfined
}