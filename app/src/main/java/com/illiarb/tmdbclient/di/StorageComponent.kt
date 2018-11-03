package com.illiarb.tmdbclient.di

import com.illiarb.tmdbclient.di.modules.NetworkModule
import com.illiarb.tmdbclient.di.modules.RepositoriesModule
import com.illiarb.tmdbclient.di.modules.StorageModule
import com.illiarb.tmdbexplorerdi.App
import com.illiarb.tmdbexplorerdi.providers.StorageProvider
import dagger.Component
import javax.inject.Singleton

/**
 * @author ilya-rb on 03.11.18.
 */
@Component(
    modules = [
        RepositoriesModule::class,
        NetworkModule::class,
        StorageModule::class
    ]
)
@Singleton
interface StorageComponent : StorageProvider {
    companion object {
        fun get(app: App): StorageProvider =
            DaggerStorageComponent.builder()
                .storageModule(StorageModule(app))
                .build()
    }
}