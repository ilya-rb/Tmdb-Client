package com.illiarb.tmdbclient.libs.tools.internal

import com.illiarb.tmdbclient.libs.tools.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * @author ilya-rb on 07.01.19.
 */
internal class CoroutineDispatcherProvider @Inject constructor() : DispatcherProvider {

  override val io: CoroutineDispatcher = Dispatchers.IO

  override val main: CoroutineDispatcher = Dispatchers.Main.immediate

  override val default: CoroutineDispatcher = Dispatchers.Default

  override val none: CoroutineDispatcher = Dispatchers.Unconfined
}