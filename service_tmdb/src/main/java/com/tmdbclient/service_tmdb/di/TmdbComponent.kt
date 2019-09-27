package com.tmdbclient.service_tmdb.di

import com.illiarb.tmdbclient.storage.di.modules.ErrorHandlingModule
import com.illiarb.tmdbclient.storage.di.modules.NetworkModule
import com.illiarb.tmdblcient.core.di.App
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
        NetworkModule::class,
        ErrorHandlingModule::class
    ]
)
interface TmdbComponent : TmdbProvider {

    companion object {

        fun get(
            app: App,
            toolsProvider: ToolsProvider,
            storageProvider: StorageProvider
        ): TmdbProvider =
            DaggerTmdbComponent.builder()
                .toolsProvider(toolsProvider)
                .storageProvider(storageProvider)
                .tmdbApiModule(TmdbApiModule(app))
                .build()
    }
}