package com.illiarb.tmdbclient.storage.di

import com.illiarb.tmdbclient.storage.di.modules.StorageModule
import com.illiarb.tmdblcient.core.app.App
import com.illiarb.tmdblcient.core.di.providers.StorageProvider
import com.illiarb.tmdblcient.core.di.providers.ToolsProvider
import dagger.Component
import javax.inject.Singleton

/**
 * @author ilya-rb on 24.12.18.
 */
@Component(
    dependencies = [ToolsProvider::class],
    modules = [StorageModule::class]
)
@Singleton
interface StorageComponent : StorageProvider {
    companion object {
        fun get(app: App, toolsProvider: ToolsProvider): StorageProvider =
            DaggerStorageComponent.builder()
                .storageModule(StorageModule(app))
                .toolsProvider(toolsProvider)
                .build()
    }
}