package com.illiarb.tmdbclient.di.modules

import com.illiarb.tmdbclient.tools.RxEventBus
import com.illiarb.tmdbclient.tools.RxSchedulerProvider
import com.illiarb.tmdblcient.core.system.EventBus
import com.illiarb.tmdblcient.core.system.SchedulerProvider
import dagger.Binds
import dagger.Module

/**
 * @author ilya-rb on 04.11.18.
 */
@Module
interface ToolsModule {

    @Binds
    fun bindRxEventBus(impl: RxEventBus): EventBus

    @Binds
    fun bindRxSchedulerProvider(impl: RxSchedulerProvider): SchedulerProvider
}