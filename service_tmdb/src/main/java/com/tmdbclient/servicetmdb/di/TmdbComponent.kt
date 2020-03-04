package com.tmdbclient.servicetmdb.di

import com.illiarb.tmdbclient.storage.di.modules.NetworkModule
import com.illiarb.tmdblcient.core.app.App
import com.illiarb.tmdblcient.core.di.providers.InteractorsProvider
import com.illiarb.tmdblcient.core.di.providers.StorageProvider
import com.illiarb.tmdblcient.core.di.providers.TmdbProvider
import com.illiarb.tmdblcient.core.di.providers.ToolsProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
  dependencies = [
    ToolsProvider::class,
    StorageProvider::class
  ],
  modules = [
    ApiModule::class,
    TmdbNetworkModule::class,
    NetworkModule::class,
    RepositoriesModule::class,
    InteractorsModule::class,
    ConfigurationModule::class
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
        .tmdbNetworkModule(TmdbNetworkModule(app))
        .build()
  }
}