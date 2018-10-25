package com.illiarb.tmdbclient.storage.di

import com.illiarb.tmdbexplorerdi.providers.StorageProvider
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        RepositoriesModule::class,
        NetworkModule::class,
        DataSourceModule::class
    ]
)
@Singleton
interface StorageComponent : StorageProvider {
    companion object {
        fun get(): StorageProvider = DaggerStorageComponent.create()
    }
}