package com.illiarb.tmdbclient.storage.di

import com.illiarb.tmdbclient.storage.di.modules.ErrorHandlingModule
import com.illiarb.tmdbclient.storage.di.modules.NetworkModule
import com.illiarb.tmdbclient.storage.di.modules.RepositoriesModule
import com.illiarb.tmdbclient.storage.di.modules.StorageModule
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.di.providers.StorageProvider
import com.illiarb.tmdblcient.core.di.providers.ToolsProvider
import dagger.Component
import javax.inject.Singleton

/**
 * @author ilya-rb on 24.12.18.
 */
@Component(
    dependencies = [ToolsProvider::class],
    modules = [
        RepositoriesModule::class,
        NetworkModule::class,
        StorageModule::class,
        ErrorHandlingModule::class
    ]
)
@Singleton
interface StorageComponent : StorageProvider {
    companion object {
        fun get(app: App, toolsProvider: ToolsProvider): StorageProvider =
            DaggerStorageComponent.builder()
                .storageModule(StorageModule(app))
                .networkModule(NetworkModule(app))
                .toolsProvider(toolsProvider)
                .build()
    }
}