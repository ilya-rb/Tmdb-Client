package com.illiarb.tmdblcient.core.di.providers

import com.illiarb.tmdblcient.core.navigation.NavigatorHolder
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.system.coroutine.DispatcherProvider

/**
 * @author ilya-rb on 24.12.18.
 */
interface ToolsProvider {

    fun provideDispatcherProvider(): DispatcherProvider

    fun provideRouter(): Router

    fun provideNavigatorHolder(): NavigatorHolder
}