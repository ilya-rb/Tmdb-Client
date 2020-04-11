package com.illiarb.tmdbclient.ui.details.di

import com.illiarb.tmdbclient.navigation.Router
import com.illiarb.tmdbclient.tools.DateFormatter
import com.illiarb.tmdbclient.ui.details.MovieDetailsFragment
import com.illiarb.tmdbexplorer.coreui.di.ViewModelModule
import com.illiarb.tmdclient.analytics.AnalyticsService
import com.tmdbclient.servicetmdb.interactor.MoviesInteractor
import dagger.BindsInstance
import dagger.Component

@Component(
  dependencies = [MovieDetailsComponent.Dependencies::class],
  modules = [MovieDetailsModule::class, ViewModelModule::class]
)
interface MovieDetailsComponent {

  interface Dependencies {
    fun router(): Router
    fun moviesInteractor(): MoviesInteractor
    fun analyticsService(): AnalyticsService
    fun dateFormatter(): DateFormatter
  }

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun movieId(id: Int): Builder
    fun dependencies(dependencies: Dependencies): Builder
    fun build(): MovieDetailsComponent
  }

  fun inject(fragment: MovieDetailsFragment)
}