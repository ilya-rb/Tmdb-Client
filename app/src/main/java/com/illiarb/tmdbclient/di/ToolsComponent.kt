package com.illiarb.tmdbclient.di

import com.illiarb.tmdbclient.di.modules.ToolsModule
import com.illiarb.tmdbexplorerdi.providers.ToolsProvider
import dagger.Component

/**
 * @author ilya-rb on 04.11.18.
 */
@Component(modules = [ToolsModule::class])
interface ToolsComponent : ToolsProvider {

    companion object {
        fun get(): ToolsProvider = DaggerToolsComponent.create()
    }
}