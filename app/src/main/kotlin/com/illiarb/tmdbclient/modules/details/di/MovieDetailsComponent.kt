package com.illiarb.tmdbclient.modules.details.di

import com.illiarb.tmdbclient.libs.ui.di.ViewModelModule
import com.illiarb.tmdbclient.modules.details.MovieDetailsFragment
import com.illiarb.tmdbclient.navigation.Router
import com.illiarb.tmdbclient.services.analytics.AnalyticsService
import com.illiarb.tmdbclient.services.tmdb.interactor.MoviesInteractor
import dagger.BindsInstance
import dagger.Component
import com.illiarb.tmdbclient.modules.details.v2.MovieDetailsFragment as ComposeMovieDetailsFragment

@Component(
  dependencies = [MovieDetailsComponent.Dependencies::class],
  modules = [
    MovieDetailsModule::class,
    ViewModelModule::class
  ]
)
interface MovieDetailsComponent {

  interface Dependencies {
    fun router(): Router
    fun moviesInteractor(): MoviesInteractor
    fun analyticsService(): AnalyticsService
  }

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance movieId: Int, dependencies: Dependencies): MovieDetailsComponent
  }

  fun inject(fragment: MovieDetailsFragment)
  fun inject(fragment: ComposeMovieDetailsFragment)
}