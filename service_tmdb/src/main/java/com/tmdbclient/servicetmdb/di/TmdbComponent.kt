package com.tmdbclient.servicetmdb.di

import com.illiarb.tmdbclient.storage.di.modules.NetworkModule
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.di.providers.InteractorsProvider
import com.illiarb.tmdblcient.core.di.providers.StorageProvider
import com.illiarb.tmdblcient.core.di.providers.TmdbProvider
import com.illiarb.tmdblcient.core.di.providers.ToolsProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [ToolsProvider::class, StorageProvider::class],
    modules = [
        TmdbModule::class,
        TmdbApiModule::class,
        TmdbApiModule.ApiModule::class,
        TmdbApiModule.ConfigModule::class,
        NetworkModule::class
    ]
)
interface TmdbComponent : TmdbProvider, InteractorsProvider {

    companion object {

        fun get(
            app: App,
            toolsProvider: ToolsProvider,
            storageProvider: StorageProvider
        ): TmdbComponent =
            DaggerTmdbComponent.builder()
                .toolsProvider(toolsProvider)
                .storageProvider(storageProvider)
                .tmdbApiModule(TmdbApiModule(app))
                .build()
    }
}