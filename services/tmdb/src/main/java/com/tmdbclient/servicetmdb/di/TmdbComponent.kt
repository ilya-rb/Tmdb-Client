package com.tmdbclient.servicetmdb.di

import android.app.Application
import com.illiarb.tmdbclient.tools.DateFormatter
import com.illiarb.tmdbclient.tools.DispatcherProvider
import com.illiarb.tmdbclient.tools.ResourceResolver
import com.illiarb.tmdbclient.tools.WorkManager
import com.tmdbclient.servicetmdb.interactor.GenresInteractor
import com.tmdbclient.servicetmdb.interactor.HomeInteractor
import com.tmdbclient.servicetmdb.interactor.MoviesInteractor
import com.tmdbclient.servicetmdb.interactor.TrendingInteractor
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
  dependencies = [TmdbComponent.Dependencies::class],
  modules = [
    ApiModule::class,
    NetworkModule::class,
    NetworkModule::class,
    RepositoriesModule::class,
    InteractorsModule::class,
    ConfigurationModule::class
  ]
)
@Singleton
interface TmdbComponent : TmdbProvider {

  interface Dependencies {
    fun dispatcherProvider(): DispatcherProvider
    fun resourceResolver(): ResourceResolver
  }

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(app: Application): Builder
    fun dependencies(dependencies: Dependencies): Builder
    fun build(): TmdbComponent
  }
}

interface TmdbProvider {
  fun homeInteractor(): HomeInteractor
  fun genresInteractor(): GenresInteractor
  fun moviesInteractor(): MoviesInteractor
  fun trendingInteractor(): TrendingInteractor
  fun dateFormatter(): DateFormatter
  fun configurationWorkerCreator(): WorkManager.WorkerCreator
}