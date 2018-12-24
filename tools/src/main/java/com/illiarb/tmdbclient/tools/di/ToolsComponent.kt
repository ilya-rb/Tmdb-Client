package com.illiarb.tmdbclient.tools.di

import com.illiarb.tmdbclient.tools.di.modules.NavigationModule
import com.illiarb.tmdbclient.tools.di.modules.ToolsModule
import com.illiarb.tmdblcient.core.di.providers.ToolsProvider
import dagger.Component
import javax.inject.Singleton

/**
 * @author ilya-rb on 24.12.18.
 */
@Component(modules = [ToolsModule::class, NavigationModule::class])
@Singleton
interface ToolsComponent : ToolsProvider {

    companion object {
        fun get(): ToolsProvider = DaggerToolsComponent.create()
    }
}