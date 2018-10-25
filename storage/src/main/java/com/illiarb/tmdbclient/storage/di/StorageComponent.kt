package com.illiarb.tmdbclient.storage.di

import com.illiarb.tmdbexplorerdi.App
import com.illiarb.tmdbexplorerdi.providers.StorageProvider
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        RepositoriesModule::class,
        NetworkModule::class,
        PersistableStorageModule::class
    ]
)
@Singleton
interface StorageComponent : StorageProvider {
    companion object {
        fun get(app: App): StorageProvider =
            DaggerStorageComponent.builder()
                .persistableStorageModule(PersistableStorageModule(app))
                .build()
    }
}