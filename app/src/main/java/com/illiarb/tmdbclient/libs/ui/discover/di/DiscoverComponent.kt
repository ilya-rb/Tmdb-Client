package com.illiarb.tmdbclient.libs.ui.discover.di

import com.illiarb.tmdbclient.navigation.Router
import com.illiarb.tmdbclient.libs.ui.discover.DiscoverFragment
import com.illiarb.tmdbclient.libs.ui.di.ViewModelModule
import com.illiarb.tmdbclient.services.analytics.AnalyticsService
import com.illiarb.tmdbclient.services.tmdb.interactor.GenresInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.MoviesInteractor
import dagger.BindsInstance
import dagger.Component

@Component(
  dependencies = [DiscoverComponent.Dependencies::class],
  modules = [
    DiscoverModule::class,
    ViewModelModule::class
  ]
)
interface DiscoverComponent {

  interface Dependencies {
    fun moviesInteractor(): MoviesInteractor
    fun genresInteractor(): GenresInteractor
    fun router(): Router
    fun analyticsService(): AnalyticsService
  }

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun genreId(id: Int): Builder
    fun dependencies(dependencies: Dependencies): Builder
    fun build(): DiscoverComponent
  }

  fun inject(fragment: DiscoverFragment)
}