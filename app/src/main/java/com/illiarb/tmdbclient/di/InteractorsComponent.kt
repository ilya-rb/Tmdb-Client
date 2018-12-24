package com.illiarb.tmdbclient.di

import com.illiarb.tmdbclient.di.modules.InteractorsModule
import com.illiarb.tmdblcient.core.di.providers.InteractorsProvider
import com.illiarb.tmdblcient.core.di.providers.StorageProvider
import com.illiarb.tmdblcient.core.di.providers.ToolsProvider
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