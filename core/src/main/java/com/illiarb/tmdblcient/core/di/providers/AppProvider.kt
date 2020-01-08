package com.illiarb.tmdblcient.core.di.providers

import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.navigation.NavigatorHolder
import com.illiarb.tmdblcient.core.navigation.Router

/**
 * @author ilya-rb on 24.12.18.
 */
interface AppProvider :
    StorageProvider,
    ToolsProvider,
    TmdbProvider,
    InteractorsProvider,
    AnalyticsProvider {

    fun getApp(): App

    fun router(): Router

    fun navigatorHolder(): NavigatorHolder
}