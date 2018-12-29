package com.illiarb.tmdbclient.tools.di.modules

import com.illiarb.tmdbclient.tools.RxSchedulerProvider
import com.illiarb.tmdblcient.core.system.SchedulerProvider
import dagger.Binds
import dagger.Module

/**
 * @author ilya-rb on 24.12.18.
 */
@Module
interface ToolsModule {

    @Binds
    fun bindRxSchedulerProvider(impl: RxSchedulerProvider): SchedulerProvider
}