package com.illiarb.tmdbclient.storage.di

import com.illiarb.tmdbclient.storage.di.modules.NetworkModule
import com.illiarb.tmdbclient.storage.di.modules.RepositoriesModule
import com.illiarb.tmdbclient.storage.di.modules.StorageModule
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.di.providers.StorageProvider
import dagger.Component
import javax.inject.Singleton

/**
 * @author ilya-rb on 24.12.18.
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