package com.illiarb.tmdbclient.tools.di

import com.illiarb.tmdbclient.tools.di.modules.AndroidModule
import com.illiarb.tmdbclient.tools.di.modules.ToolsModule
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.di.providers.ToolsProvider
import dagger.Component
import javax.inject.Singleton

/**
 * @author ilya-rb on 24.12.18.
 */
@Component(
    modules = [
        ToolsModule::class,
        AndroidModule::class
    ]
)
@Singleton
interface ToolsComponent : ToolsProvider {

    companion object {
        fun get(app: App): ToolsProvider =
            DaggerToolsComponent.builder()
                .androidModule(AndroidModule(app))
                .build()
    }
}