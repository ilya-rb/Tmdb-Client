package com.illiarb.tmdblcient.core.di.providers

import com.illiarb.tmdblcient.core.tools.ConnectivityStatus
import com.illiarb.tmdblcient.core.tools.DispatcherProvider

/**
 * @author ilya-rb on 24.12.18.
 */
interface ToolsProvider {

    fun provideDispatcherProvider(): DispatcherProvider

    fun provideConnectivityStatus(): ConnectivityStatus
}