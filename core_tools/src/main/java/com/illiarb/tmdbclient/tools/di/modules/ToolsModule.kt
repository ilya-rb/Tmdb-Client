package com.illiarb.tmdbclient.tools.di.modules

import com.illiarb.tmdbclient.tools.AppWorkManager
import com.illiarb.tmdbclient.tools.CoroutineDispatcherProvider
import com.illiarb.tmdblcient.core.tools.DispatcherProvider
import com.illiarb.tmdblcient.core.tools.WorkManager
import dagger.Binds
import dagger.Module

/**
 * @author ilya-rb on 24.12.18.
 */
@Module
interface ToolsModule {

    @Binds
    fun bindDispatcherProvider(impl: CoroutineDispatcherProvider): DispatcherProvider

    @Binds
    fun bindWorkManager(appWorkManager: AppWorkManager): WorkManager
}