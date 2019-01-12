package com.illiarb.tmdbclient.tools.di.modules

import com.illiarb.tmdbclient.tools.CoroutineDispatcherProvider
import com.illiarb.tmdblcient.core.system.coroutine.DispatcherProvider
import dagger.Binds
import dagger.Module

/**
 * @author ilya-rb on 24.12.18.
 */
@Module
interface ToolsModule {

    @Binds
    fun bindDispatcherProvider(impl: CoroutineDispatcherProvider): DispatcherProvider
}