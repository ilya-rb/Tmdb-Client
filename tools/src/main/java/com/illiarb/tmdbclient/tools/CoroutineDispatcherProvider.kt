package com.illiarb.tmdbclient.tools

import com.illiarb.tmdblcient.core.tools.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * @author ilya-rb on 07.01.19.
 */
@ExperimentalCoroutinesApi
class CoroutineDispatcherProvider @Inject constructor() :
    DispatcherProvider {

    override val io: CoroutineDispatcher = Dispatchers.IO

    override val main: CoroutineDispatcher = Dispatchers.Main

    override val default: CoroutineDispatcher = Dispatchers.Default

    override val none: CoroutineDispatcher = Dispatchers.Unconfined
}