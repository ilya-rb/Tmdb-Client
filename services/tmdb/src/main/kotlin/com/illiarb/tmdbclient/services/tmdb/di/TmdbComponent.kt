package com.illiarb.tmdbclient.services.tmdb.di

import android.app.Application
import com.illiarb.tmdbclient.libs.tools.DateFormatter
import com.illiarb.tmdbclient.libs.tools.DispatcherProvider
import com.illiarb.tmdbclient.libs.tools.ResourceResolver
import com.illiarb.tmdbclient.libs.tools.WorkManager
import com.illiarb.tmdbclient.services.tmdb.interactor.GenresInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.HomeInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.MoviesInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.TrendingInteractor
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