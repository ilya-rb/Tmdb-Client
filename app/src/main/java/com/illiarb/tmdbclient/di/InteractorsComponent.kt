package com.illiarb.tmdbclient.di

import com.illiarb.tmdbclient.di.modules.InteractorsModule
import com.illiarb.tmdbexplorerdi.providers.InteractorsProvider
import com.illiarb.tmdbexplorerdi.providers.StorageProvider
import com.illiarb.tmdbexplorerdi.providers.ToolsProvider
import dagger.Component

@Component(
    dependencies = [
        StorageProvider::class,
        ToolsProvider::class
    ],
    modules = [InteractorsModule::class]
)
interface InteractorsComponent : InteractorsProvider {

    companion object {

        fun get(storageProvider: StorageProvider, toolsProvider: ToolsProvider): InteractorsProvider {
            return DaggerInteractorsComponent.builder()
                .storageProvider(storageProvider)
                .toolsProvider(toolsProvider)
                .build()
        }
    }
}