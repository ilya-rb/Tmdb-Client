package com.illiarb.tmdbclient.storage.di

import com.illiarb.tmdbexplorerdi.App
import com.illiarb.tmdbexplorerdi.providers.StorageProvider
import dagger.Component
import javax.inject.Singleton

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