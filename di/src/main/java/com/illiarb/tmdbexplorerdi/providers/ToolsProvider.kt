package com.illiarb.tmdbexplorerdi.providers

import com.illiarb.tmdblcient.core.navigation.NavigatorHolder
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.system.SchedulerProvider

/**
 * @author ilya-rb on 04.11.18.
 */
interface ToolsProvider {

    fun provideSchedulerProvider(): SchedulerProvider

    fun provideRouter(): Router

    fun provideNavigatorHolder(): NavigatorHolder
}