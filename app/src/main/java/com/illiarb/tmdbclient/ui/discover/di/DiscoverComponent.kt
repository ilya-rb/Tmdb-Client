package com.illiarb.tmdbclient.ui.discover.di

import com.illiarb.tmdbclient.navigation.Router
import com.illiarb.tmdbclient.ui.discover.DiscoverFragment
import com.illiarb.tmdbexplorer.coreui.di.ViewModelModule
import com.illiarb.tmdclient.analytics.AnalyticsService
import com.tmdbclient.servicetmdb.interactor.GenresInteractor
import com.tmdbclient.servicetmdb.interactor.MoviesInteractor
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