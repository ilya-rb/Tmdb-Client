package com.illiarb.tmdbclient.services.tmdb.di

import android.app.Application
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.illiarb.tmdbclient.libs.buildconfig.BuildConfig
import com.illiarb.tmdbclient.libs.buildconfig.TmdbConfig
import com.illiarb.tmdbclient.libs.tools.DispatcherProvider
import com.illiarb.tmdbclient.libs.tools.ResourceResolver
import com.illiarb.tmdbclient.services.tmdb.interactor.DiscoverInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.FiltersInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.GenresInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.HomeInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.MoviesInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.SearchInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.TrendingInteractor
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
  dependencies = [TmdbComponent.Dependencies::class],
  modules = [
    ApiModule::class,
    NetworkModule::class,
    RepositoriesModule::class,
    DatabaseModule::class,
    InteractorsModule::class,
    ConfigurationModule::class,
    FlipperModule::class
  ]
)
@Singleton
interface TmdbComponent : TmdbProvider {

  interface Dependencies {
    fun dispatcherProvider(): DispatcherProvider
    fun resourceResolver(): ResourceResolver
    fun buildConfig(): BuildConfig
    fun tmdbConfig(): TmdbConfig
  }

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance app: Application, dependencies: Dependencies): TmdbComponent
  }
}

interface TmdbProvider {
  fun homeInteractor(): HomeInteractor
  fun genresInteractor(): GenresInteractor
  fun moviesInteractor(): MoviesInteractor
  fun discoverInteractor(): DiscoverInteractor
  fun searchInteractor(): SearchInteractor
  fun trendingInteractor(): TrendingInteractor
  fun filtersInteractor(): FiltersInteractor
  fun networkFlipperPlugin(): NetworkFlipperPlugin
}